package com.kotula.nikolai.trainingpeakscodetest.repos.concrete;

import android.util.JsonReader;
import android.util.JsonToken;
import android.util.Log;

import com.kotula.nikolai.trainingpeakscodetest.data.PeakHeartRate;
import com.kotula.nikolai.trainingpeakscodetest.data.PeakSpeed;
import com.kotula.nikolai.trainingpeakscodetest.repos.interfaces.IWorkoutRepo;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

/**
 * Concrete implementation of the data source for Workout data.
 */
public class HttpWorkoutRESTClient implements IWorkoutRepo {
    private static final String TAG = "HttpWorkoutRESTClient";
    private static final String ENDPOINT = "https://tpapi.trainingpeaks.com/public/v1/workouts/";
    private static final String NAME_PEAK_SPEEDS = "peakSpeeds";
    private static final String NAME_PEAK_HEART_RATES = "peakHeartRates";
    private static final String NAME_BEGIN = "begin";
    private static final String NAME_END = "end";
    private static final String NAME_INTERVAL = "interval";
    private static final String NAME_VALUE = "value";

    private ArrayList<PeakHeartRate> mPeakHeartRates = new ArrayList<>();
    private ArrayList<PeakSpeed> mPeakSpeeds = new ArrayList<>();

    /**
     * Fetches a {@link List} of {@link PeakHeartRate} objects from the data source.
     * @return The {@link List} of {@link PeakHeartRate} objects from the data source.
     */
    @Override
    public List<PeakHeartRate> getPeakHeartRates(String workoutTag) {
        Log.d(TAG, "getPeakHeartRates()");
        fetchData(workoutTag);
        return mPeakHeartRates;
    }

    /**
     * Fetches a {@link List} of {@link PeakSpeed} objects from the data source.
     * @return The {@link List} of {@link PeakSpeed} objects from the data source.
     */
    @Override
    public List<PeakSpeed> getPeakSpeeds(String workoutTag) {
        Log.d(TAG, "getPeakSpeeds()");
        fetchData(workoutTag);
        return mPeakSpeeds;
    }

    private void parseResult(JsonReader reader) throws IOException, IllegalStateException {
        if (reader == null)
            return;

        while (reader.hasNext()) {
            JsonToken peekVal = reader.peek();

            if (peekVal == JsonToken.END_OBJECT)
                return;

            if (peekVal == JsonToken.NAME) {

                String name = reader.nextName();

                // If the name is something we're looking for, begin assuming JSON formatting:
                if (NAME_PEAK_HEART_RATES.equals(name) || NAME_PEAK_SPEEDS.equals(name)) {
                    if (reader.peek() == JsonToken.BEGIN_ARRAY) {

                        // Begin the array:
                        reader.beginArray();

                        if (NAME_PEAK_HEART_RATES.equals(name)) {
                            peakHeartRatesFromJson(reader);
                        } else if (NAME_PEAK_SPEEDS.equals(name)) {
                            peakSpeedsFromJson(reader);
                        }

                        // Don't forget to end the array when we're done:
                        reader.endArray();
                    }
                }

            } else if (peekVal == JsonToken.BEGIN_OBJECT) {

                // We found an object, so recursively search it for 'peakSpeeds':
                reader.beginObject();
                parseResult(reader);
                reader.endObject();

            } else if (peekVal == JsonToken.BEGIN_ARRAY) {

                // Begin the Array:
                reader.beginArray();

                // Inspect the next value:
                peekVal = reader.peek();

                if (peekVal == JsonToken.BEGIN_OBJECT) {
                    while (reader.hasNext() && (reader.peek() != JsonToken.END_ARRAY)) {

                        // We found an array of objects.  Inspect them all!
                        reader.beginObject();
                        parseResult(reader);
                        reader.endObject();
                    }
                } else if (peekVal == JsonToken.BEGIN_ARRAY) {
                    while (reader.hasNext() && (reader.peek() != JsonToken.END_ARRAY)) {
                        // We found an array of arrays.  For now, just skip the darn thing.
                        // TODO:  Recursively check arrays for what we're looking for.
                        reader.skipValue();
                    }
                } else {
                    // we have found an array of values, so skip them all:
                    while (reader.hasNext() && (reader.peek() != JsonToken.END_ARRAY)) {
                        reader.skipValue();
                    }
                }

                // Don't forget to end the array:
                reader.endArray();
            } else {

                // Default case:
                reader.skipValue();
            }
        }
    }

    private void peakSpeedsFromJson(JsonReader reader) throws IOException, IllegalStateException {
        Log.d(TAG, "peakSpeedsFromJson()");

        while (reader.hasNext() && (reader.peek() != JsonToken.END_ARRAY)) {
            long beginVal = -1;
            long endVal = -1;
            int intervalVal = -1;
            double valueVal = -1.0;

            reader.beginObject();

            while (reader.hasNext() && (reader.peek() != JsonToken.END_OBJECT)) {
                switch (reader.nextName()) {
                    case NAME_BEGIN:
                        beginVal = reader.nextLong();
                        break;
                    case NAME_END:
                        endVal = reader.nextLong();
                        break;
                    case NAME_INTERVAL:
                        intervalVal = reader.nextInt();
                        break;
                    case NAME_VALUE:
                        valueVal = reader.nextDouble();
                        break;
                    default:
                        break;
                }
            }

            reader.endObject();

            mPeakSpeeds.add(new PeakSpeed(beginVal, endVal, intervalVal, valueVal));
        }
    }

    private void peakHeartRatesFromJson(JsonReader reader) throws IOException, IllegalStateException {
        Log.d(TAG, "peakHeartRatesFromJson()");

        while (reader.hasNext() && (reader.peek() != JsonToken.END_ARRAY)) {
            long beginVal = -1;
            long endVal = -1;
            int intervalVal = -1;
            int valueVal = -1;

            reader.beginObject();

            while (reader.hasNext() && (reader.peek() != JsonToken.END_OBJECT)) {
                switch (reader.nextName()) {
                    case NAME_BEGIN:
                        beginVal = reader.nextLong();
                        break;
                    case NAME_END:
                        endVal = reader.nextLong();
                        break;
                    case NAME_INTERVAL:
                        intervalVal = reader.nextInt();
                        break;
                    case NAME_VALUE:
                        valueVal = reader.nextInt();
                        break;
                    default:
                        break;
                }
            }

            reader.endObject();

            mPeakHeartRates.add(new PeakHeartRate(beginVal, endVal, intervalVal, valueVal));
        }
    }

    private void fetchData(String workoutTag) {
        // Reset old data if we still have it:
        mPeakHeartRates.clear();
        mPeakSpeeds.clear();

        URL endpoint = null;
        HttpsURLConnection urlConnection = null;

        // Assign the URL object:
        try {
            endpoint = new URL(ENDPOINT.concat(workoutTag));
        } catch (MalformedURLException ex) {
            Log.e(TAG, ex.getMessage());
            return;
        }

        InputStream is = null;
        InputStreamReader streamReader = null;
        JsonReader jsonReader = null;

        // Open the TCP connection and read the input stream:
        try {
            urlConnection = (HttpsURLConnection)endpoint.openConnection();
            is = urlConnection.getInputStream();
            streamReader = new InputStreamReader(is, "UTF-8");
            jsonReader = new JsonReader(streamReader);
            jsonReader.beginObject();
            parseResult(jsonReader);
        } catch (IOException ex) {
            Log.e(TAG, ex.getMessage());
            return;
        } catch (IllegalStateException ex) {
            Log.e(TAG, ex.getMessage());
            Log.e(TAG, "Malformed JSON!");
            return;
        } finally {
            // Close the readers:
            if (jsonReader != null) {
                try {
                    jsonReader.close();
                } catch (IOException ex) {
                    Log.e(TAG, "fetchData() - Failed to close JSON Reader!");
                }
            }
            if (streamReader != null) {
                try {
                    streamReader.close();
                } catch (IOException ex) {
                    Log.e(TAG, "fetchData() - Failed to close Stream Reader!");
                }
            }
            if (is != null) {
                try {
                    is.close();
                } catch (IOException ex) {
                    Log.e(TAG, "fetchData() - Failed to close Input Stream!");
                }
            }
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
    }
}

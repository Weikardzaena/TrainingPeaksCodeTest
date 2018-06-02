package com.kotula.nikolai.trainingpeakscodetest.repos.concrete;

import android.util.JsonReader;
import android.util.JsonToken;
import android.util.Log;

import com.kotula.nikolai.trainingpeakscodetest.data.PeakHeartRate;
import com.kotula.nikolai.trainingpeakscodetest.data.PeakSpeed;
import com.kotula.nikolai.trainingpeakscodetest.repos.interfaces.IWorkoutRepo;

import java.io.IOException;
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

    /**
     * Fetches a {@link List} of {@link PeakHeartRate} objects from the data source.
     * @return The {@link List} of {@link PeakHeartRate} objects from the data source.
     */
    @Override
    public List<PeakHeartRate> getPeakHeartRates(String workoutTag) {
        Log.d(TAG, "getPeakHeartRates()");

        URL endpoint;
        HttpsURLConnection urlConnection;
        StringBuilder response = new StringBuilder();

        // Assign the URL object:
        try {
            endpoint = new URL(ENDPOINT.concat(workoutTag));
        } catch (MalformedURLException ex) {
            Log.e(TAG, ex.getMessage());
            return null;
        }

        // Open the TCP connection:
        try {
            urlConnection = (HttpsURLConnection)endpoint.openConnection();
        } catch (IOException ex) {
            Log.e(TAG, ex.getMessage());
            return null;
        }

        ArrayList<PeakHeartRate> heartRates = new ArrayList<PeakHeartRate>();

        // Read the buffer:
        try {
            JsonReader reader = new JsonReader(new InputStreamReader(urlConnection.getInputStream(), "UTF-8"));
        } catch (IOException ex) {
            Log.e(TAG, ex.getMessage());
            return null;
        } catch (IllegalStateException ex) {
            Log.e(TAG, ex.getMessage());
            Log.e(TAG, "Malformed JSON!");
            return null;
        }

        Log.d(TAG, response.toString());
        return heartRates;
    }

    /**
     * Fetches a {@link List} of {@link PeakSpeed} objects from the data source.
     * @return The {@link List} of {@link PeakSpeed} objects from the data source.
     */
    @Override
    public List<PeakSpeed> getPeakSpeeds(String workoutTag) {
        Log.d(TAG, "getPeakSpeeds()");

        URL endpoint;
        HttpsURLConnection urlConnection;

        // Assign the URL object:
        try {
            endpoint = new URL(ENDPOINT.concat(workoutTag));
        } catch (MalformedURLException ex) {
            Log.e(TAG, ex.getMessage());
            return null;
        }

        // Open the TCP connection:
        try {
            urlConnection = (HttpsURLConnection)endpoint.openConnection();
        } catch (IOException ex) {
            Log.e(TAG, ex.getMessage());
            return null;
        }

        ArrayList<PeakSpeed> peakSpeeds = new ArrayList<>();

        // Read the buffer:
        try {
            JsonReader reader = new JsonReader(new InputStreamReader(urlConnection.getInputStream(), "UTF-8"));
            reader.beginObject();
            parsePeakSpeeds(reader, peakSpeeds);
        } catch (IOException ex) {
            Log.e(TAG, ex.getMessage());
            return null;
        } catch (IllegalStateException ex) {
            Log.e(TAG, ex.getMessage());
            Log.e(TAG, "Malformed JSON!");
            return null;
        }

        return peakSpeeds;
    }

    private void parsePeakSpeeds(JsonReader reader, ArrayList<PeakSpeed> outSpeeds) throws IOException, IllegalStateException {
        if ((reader == null) || (outSpeeds == null))
            return;

        while (reader.hasNext()) {
            JsonToken peekVal = reader.peek();
            if (peekVal == JsonToken.NAME) {

                // If the name is 'peakSpeeds' AND the next value is the start of an array, dive in:
                if ((NAME_PEAK_SPEEDS.equals(reader.nextName())) && (reader.peek() == JsonToken.BEGIN_ARRAY)) {

                    // Begin the array:
                    reader.beginArray();

                    // Iterate over the array:
                    while (reader.hasNext() && (reader.peek() != JsonToken.END_ARRAY)) {
                        if (reader.peek() == JsonToken.BEGIN_OBJECT) {

                            // We have found a 'peakSpeeds' object (hopefully), so parse it:
                            reader.beginObject();
                            outSpeeds.add(peakSpeedFromJson(reader));
                            reader.endObject();
                        } else {

                            // If we find anything not an object in 'peakSpeeds', skip it.
                            reader.skipValue();
                        }
                    }

                    // Don't forget to end the array when we're done:
                    reader.endArray();
                }

            } else if (peekVal == JsonToken.BEGIN_OBJECT) {

                // We found an object, so recursively search it for 'peakSpeeds':
                reader.beginObject();
                parsePeakSpeeds(reader, outSpeeds);
                reader.endObject();

            } else if (peekVal == JsonToken.BEGIN_ARRAY) {

                // Begin the Array:
                reader.beginArray();

                // Inspect the next value:
                peekVal = reader.peek();

                // If the array contains NEITHER objects NOR arrays (a.k.a. if the array is values like numbers), skip the entire array:
                if (!((peekVal == JsonToken.BEGIN_OBJECT) || (peekVal == JsonToken.BEGIN_ARRAY))) {
                    while (reader.hasNext() && (reader.peek() != JsonToken.END_ARRAY)) {
                        reader.skipValue();
                    }
                } else {
                    if (peekVal == JsonToken.BEGIN_OBJECT) {
                        while (reader.hasNext() && (reader.peek() == JsonToken.BEGIN_OBJECT)) {

                            // We found an array of objects.  Inspect them all!
                            reader.beginObject();
                            parsePeakSpeeds(reader, outSpeeds);
                            reader.endObject();
                        }
                    } else {
                        // We found an array of arrays.  For now, just skip the damn thing.
                        // TODO:  Recursively check arrays for what we're looking for.
                        while (reader.hasNext() && (reader.peek() == JsonToken.BEGIN_ARRAY)) {
                            reader.skipValue();
                        }
                    }
                }

                // Don't forget to end the array:
                reader.endArray();
            } else {
                reader.skipValue();
            }
        }
    }

    private PeakSpeed peakSpeedFromJson(JsonReader reader) throws IOException, IllegalStateException {
        long beginVal = 0;
        long endVal = 0;
        int intervalVal = 0;
        double valueVal = 0.0;

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

        return new PeakSpeed(beginVal, endVal, intervalVal, valueVal);
    }
}

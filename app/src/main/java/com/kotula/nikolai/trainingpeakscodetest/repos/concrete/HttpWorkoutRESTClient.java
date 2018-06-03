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
     * <p/>
     * This is a BLOCKING operation.
     * @param workoutTag The Workout Tag to fetch from the endpoint.
     * @return The {@link List} of {@link PeakHeartRate} objects from the data source.
     */
    @Override
    public List<PeakHeartRate> getPeakHeartRates(String workoutTag) {
        Log.d(TAG, "getPeakSpeeds()");
        fetchData(workoutTag);
        return mPeakHeartRates;
    }

    /**
     * Fetches a {@link List} of {@link PeakSpeed} objects from the data source.
     * <p/>
     * This is a BLOCKING operation.
     * @param workoutTag The Workout Tag to fetch from the endpoint.
     * @return The {@link List} of {@link PeakSpeed} objects from the data source.
     */
    @Override
    public List<PeakSpeed> getPeakSpeeds(String workoutTag) {
        Log.d(TAG, "getPeakSpeeds()");
        fetchData(workoutTag);
        return mPeakSpeeds;
    }

    /**
     * Parses the entire JSON document for Peak Heart Rates and Peak Speeds.
     * <p>
     *     NOTE:  This method assumes that the reader is at a valid starting position.  I.E.
     *     reader.beginObject() must be called before this method is invoked.
     * </p>
     * <p>
     *     This algorithm is basically a depth-first tree search.
     * </p>
     * <p>
     *     TODO:  Nested JSON ARRAYS are not handled well, so it is possible to miss data.
     * </p>
     * @param reader The {@link JsonReader} that begins the JSON DOCUMENT.
     * @throws IOException
     * @throws IllegalStateException
     */
    private void parseResult(JsonReader reader) throws IOException, IllegalStateException {
        if (reader == null)
            return;

        while (reader.hasNext()) {
            // Peek the next JSON type:
            JsonToken peekVal = reader.peek();

            // If we have reached the end of the OBJECT, return.
            if (peekVal == JsonToken.END_OBJECT)
                return;

            // If we find a NAME, inspect it to see if it matches the NAMES we are looking for:
            if (peekVal == JsonToken.NAME) {

                String name = reader.nextName();

                // If the NAME is something we're looking for, dive in:
                if (NAME_PEAK_HEART_RATES.equals(name) || NAME_PEAK_SPEEDS.equals(name)) {

                    // Past this point, we are assuming the format of the section as an ARRAY of OBJECTS:
                    if (reader.peek() == JsonToken.BEGIN_ARRAY) {

                        // Begin the ARRAY:
                        reader.beginArray();

                        // Parse the appropriate data:
                        if (NAME_PEAK_HEART_RATES.equals(name)) {
                            peakHeartRatesFromJson(reader);
                        } else if (NAME_PEAK_SPEEDS.equals(name)) {
                            peakSpeedsFromJson(reader);
                        }

                        // Don't forget to end the ARRAY when we're done:
                        reader.endArray();
                    }
                }

            } else if (peekVal == JsonToken.BEGIN_OBJECT) {

                // We found an OBJECT, so recursively search it for 'peakSpeeds':
                reader.beginObject();
                parseResult(reader); // Recursive call
                reader.endObject();

            } else if (peekVal == JsonToken.BEGIN_ARRAY) {

                /* ARRAY are dealt with specially because they can contain any type of VALUE. */

                // Begin the ARRAY:
                reader.beginArray();

                // Inspect the first VALUE of the ARRAY:
                peekVal = reader.peek();

                if (peekVal == JsonToken.BEGIN_OBJECT) {
                    while (reader.hasNext() && (reader.peek() != JsonToken.END_ARRAY)) {

                        // We found an ARRAY of OBJECTS.  Inspect them all!
                        reader.beginObject();
                        parseResult(reader);
                        reader.endObject();
                    }
                } else if (peekVal == JsonToken.BEGIN_ARRAY) {
                    while (reader.hasNext() && (reader.peek() != JsonToken.END_ARRAY)) {
                        // We found an ARRAY of ARRAYS.  For now, just skip the darn thing.
                        // TODO:  Recursively check arrays for what we're looking for.
                        reader.skipValue();
                    }
                } else {
                    // we have found an ARRAY of other types, so skip them all:
                    while (reader.hasNext() && (reader.peek() != JsonToken.END_ARRAY)) {
                        reader.skipValue();
                    }
                }

                // Don't forget to end the ARRAY:
                reader.endArray();
            } else {

                // Default case:
                reader.skipValue();
            }
        }
    }

    /**
     * Populates the {@link ArrayList<PeakSpeed>} of Peak Speed POJOs from the provided JSON ARRAY.
     * <p>
     *     NOTE:  This method assumes that the reader is at a valid starting position.  I.E.
     *     reader.beginArray() must be called before this method is invoked.
     * </p>
     * @param reader The {@link JsonReader} that begins the JSON ARRAY.
     * @throws IOException
     * @throws IllegalStateException
     */
    private void peakSpeedsFromJson(JsonReader reader) throws IOException, IllegalStateException {
        Log.d(TAG, "peakSpeedsFromJson()");

        // Iterate over the entire ARRAY:
        while (reader.hasNext() && (reader.peek() != JsonToken.END_ARRAY)) {

            // Initialize dummy values so that missing data can be easily seen later:
            long beginVal = -1;
            long endVal = -1;
            int intervalVal = -1;
            double valueVal = -1.0;

            reader.beginObject();

            // Iterate over the entire OBJECT, pulling out values as we find them.
            // NOTE: this assumes proper formatting of the JSON, and will throw an exception if the
            //       VALUE data type does not match the expected type.
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

    /**
     * Populates the {@link ArrayList<PeakHeartRate>} of Peak Heart Rate POJOs from the provided JSON ARRAY.
     * <p>
     *     NOTE:  This method assumes that the reader is at a valid starting position.  I.E.
     *     reader.beginArray() must be called before this method is invoked.
     * </p>
     * @param reader The {@link JsonReader} that begins the JSON ARRAY.
     * @throws IOException
     * @throws IllegalStateException
     */
    private void peakHeartRatesFromJson(JsonReader reader) throws IOException, IllegalStateException {
        Log.d(TAG, "peakHeartRatesFromJson()");

        // Iterate over the entire ARRAY:
        while (reader.hasNext() && (reader.peek() != JsonToken.END_ARRAY)) {

            // Initialize dummy values so that missing data can be easily seen later:
            long beginVal = -1;
            long endVal = -1;
            int intervalVal = -1;
            int valueVal = -1;

            reader.beginObject();

            // Iterate over the entire OBJECT, pulling out values as we find them.
            // NOTE: this assumes proper formatting of the JSON, and will throw an exception if the
            //       VALUE data type does not match the expected type.
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

    /**
     * Populates the lists of Peak Heart Rates and Peak Speeds from the JSON provided at the REST endpoint.
     * @param workoutTag The Workout Tag to use in the REST request.
     */
    private void fetchData(String workoutTag) {
        // Reset old data if we still have it:
        mPeakHeartRates.clear();
        mPeakSpeeds.clear();

        URL endpoint = null;
        HttpsURLConnection urlConnection = null;

        // Assign the URL object:
        try {
            endpoint = new URL(ENDPOINT.concat((workoutTag == null) ? "" : workoutTag));
        } catch (MalformedURLException ex) {
            Log.e(TAG, ex.getMessage());
            return;
        }

        // I'm not sure whether closing the JsonReader will close the underlying InputStream as well
        // so I'm defining all the steps and closing each one just to be safe.
        InputStream inputStream = null;
        InputStreamReader streamReader = null;
        JsonReader jsonReader = null;

        // Open the TCP connection and read the input stream:
        // TODO:  Handle error responses from the connection (e.g. 404, etc.).
        try {
            // Open the TCP connection:
            urlConnection = (HttpsURLConnection)endpoint.openConnection();

            // Initialize the streams:
            inputStream = urlConnection.getInputStream();
            streamReader = new InputStreamReader(inputStream, "UTF-8");
            jsonReader = new JsonReader(streamReader);

            // Don't forget to begin the JSON OBJECT before calling parseResult()!
            jsonReader.beginObject();

            // Parse the JSON
            parseResult(jsonReader);

        } catch (IOException ex) {
            // A stream read error has occurred:
            Log.e(TAG, ex.getMessage());
        } catch (IllegalStateException ex) {
            // This is thrown when the JsonReader tries to read something it doesn't understand:
            Log.e(TAG, ex.getMessage());
            Log.e(TAG, "Malformed JSON!");
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
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException ex) {
                    Log.e(TAG, "fetchData() - Failed to close Input Stream!");
                }
            }

            // Finally, close the connection:
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
    }
}

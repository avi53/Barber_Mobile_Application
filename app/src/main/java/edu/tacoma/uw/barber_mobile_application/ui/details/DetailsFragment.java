package edu.tacoma.uw.barber_mobile_application.ui.details;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.util.List;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import edu.tacoma.uw.barber_mobile_application.R;
import edu.tacoma.uw.barber_mobile_application.databinding.FragmentDetailsBinding;

public class DetailsFragment extends Fragment {

    private GoogleMap mMap;
    private SharedPreferences sharedPreferences;
    private Cipher cipher;
    private SecretKey secretKey;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        View view = inflater.inflate(R.layout.fragment_details, container, false);
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.google_map);

        // Image buttons for social media links
        ImageButton instagramButton = view.findViewById(R.id.details_instagram);
        ImageButton facebookButton = view.findViewById(R.id.details_facebook);

        // onClick listeners for when either image button is clicked
        instagramButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uriUrl = Uri.parse("https://www.instagram.com/theempirebarbershop/");
                Intent webView = new Intent(Intent.ACTION_VIEW, uriUrl);
                startActivity(webView);            }
        });
        facebookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uriUrl = Uri.parse("https://www.facebook.com/TheEmpireBarbershop/");
                Intent webView = new Intent(Intent.ACTION_VIEW, uriUrl);
                startActivity(webView);
            }
        });

        // Sets up necessary values for encryption/decryption of API Key
        initializeCipher();
        initializeSecretKey();
        initializeSharedPreferences();

        // Encrypting + Storage of API Key
        try {
            final String mapsApiKey = "AIzaSyBvENVXmqiJ62y0Pkmv3ynWjham9SGO2hM";
            storeEncryptApiKey(mapsApiKey);
        } catch (InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
            throw new RuntimeException(e);
        }

        //  Decrypt + initialize Google API Key
        try {
            String decryptedMapsApiKey = getAndDecryptApiKey();
            updateStringsFileWithApiKey(decryptedMapsApiKey);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        assert mapFragment != null;
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            // Will set up a zoomed in map at the shops location when fragment is opened.
            @Override
            public void onMapReady(@NonNull GoogleMap googleMap) {
                mMap = googleMap;
                String address = "33100 Pacific Hwy S, Suite 10, Federal Way, Wa 98003";
                LatLng location = getLocationFromAddress(address);

                if (location != null) {
                    mMap.addMarker(new MarkerOptions().position(location).title("The Empire Barbershop"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(location));
                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15));
                    googleMap.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);
                }
            }
        });

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }


    // All Private Helper Methods

    // Obtains Lat/Long coordinates from physical address
    private LatLng getLocationFromAddress(String strAddress) {
        Geocoder geocoder = new Geocoder(getContext());
        List<Address> address;
        LatLng coordinates = null;

        try {
            address = geocoder.getFromLocationName(strAddress, 5);
            if (address == null || address.isEmpty()) {
                return null; // Address not found
            }
            Address location = address.get(0);  // Address found
            coordinates = new LatLng(location.getLatitude(), location.getLongitude());
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return coordinates;
    }

    // Initializes the Cipher for both Encryption and Decryption
    private void initializeCipher() {
        try {
            cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Initializes the Secret Key for both Encryption and Decryption
    private void initializeSecretKey() {
        try {
            // Check if the key already exists in the Keystore
            KeyStore keyStore = KeyStore.getInstance("AndroidKeyStore");
            keyStore.load(null);

            if (!keyStore.containsAlias("MyKeyAlias")) {
                // Generate a new key if it doesn't exist
                KeyGenerator keyGen = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore");
                KeyGenParameterSpec keyParamSpec = new KeyGenParameterSpec.Builder(
                        "MyKeyAlias",
                        KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT)
                        .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                        .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7)
                        .setRandomizedEncryptionRequired(false)
                        .build();
                keyGen.init(keyParamSpec);
                secretKey = keyGen.generateKey();
            } else {
                secretKey = (SecretKey) keyStore.getKey("MyKeyAlias", null);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Initializes the Shared Preferences for both Encryption and Decryption
    private void initializeSharedPreferences() {
        try {
            sharedPreferences = getContext().getSharedPreferences("map_preferences", Context.MODE_PRIVATE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Storage and Encryption of API Key
    private void storeEncryptApiKey(String apiKey)
            throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        // Encryption process
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] encryptedApiKey = cipher.doFinal(apiKey.getBytes());

        // Storing process
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("encryptedApiKey", Base64.encodeToString(encryptedApiKey, Base64.DEFAULT));
        editor.apply();
    }

    // Decryption of API Key
    private String getAndDecryptApiKey() {
        try {
            // Load the key from the Android Keystore using the alias
            KeyStore keyStore = KeyStore.getInstance("AndroidKeyStore");
            keyStore.load(null);
            secretKey = (SecretKey) keyStore.getKey("MyKeyAlias", null);

            // Get the encrypted API Key from Shared Preference
            String encryptedApiKeyString = sharedPreferences.getString("encryptedApiKey", null);
            byte[] encryptedApiKey = Base64.decode(encryptedApiKeyString, Base64.DEFAULT);

            // Decryption process
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            byte[] decryptedApiKey = cipher.doFinal(encryptedApiKey);

            return new String(decryptedApiKey);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Updates string.xml file with new decrypted API key
    private void updateStringsFileWithApiKey(String apiKey) {
        try {
            Resources resource = getResources();
            // Obtains string.xml ID
            int stringID = resource.getIdentifier(
                    "MAPS_API_KEY",
                    "string",
                    getActivity().getPackageName());
            if (stringID != 0) {
                resource.getString(stringID);
                resource.getString(stringID, apiKey);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

package com.bextdev.Texty;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.content.Context;
import com.google.appinventor.components.annotations.SimpleFunction;
import com.google.appinventor.components.annotations.SimpleProperty;
import com.google.appinventor.components.annotations.SimpleEvent;
import com.google.appinventor.components.runtime.Texting;
import com.google.appinventor.components.runtime.util.YailList;
import com.google.appinventor.components.runtime.ComponentContainer;
import com.google.appinventor.components.runtime.EventDispatcher;
import com.google.appinventor.components.runtime.AndroidNonvisibleComponent;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class Texty extends AndroidNonvisibleComponent {
    private Context context;
    private Texting texting; // Texting component set by the user
    private String lastSentMessage = "";
    private final Map<String, List<String>> messageHistory = new HashMap<>();
    private boolean isSending = false;
    private boolean errorHandlingEnabled = true;
    private static final int MAX_RETRIES = 3;

    // Properties
    private String defaultPrefix = "";
    private String defaultSuffix = "";
    private String customDelimiter = " | ";
    private int maxMessageLength = 160;
    private String characterReplacementTarget = "";
    private String characterReplacementValue = "";
    private boolean textWrappingEnabled = true;
    private List<String> prohibitedWords = new YailList();
    private boolean alternateCapitalizationEnabled = false;
    private String messageCaseFilter = "None"; // "Uppercase", "Lowercase", "Mixed", "None"
    private boolean autoTrimWhitespace = false; // Fix for missing variable

    // Constructor
    public Texty(ComponentContainer container) {
        super(container.$form());
        this.context = container.$context();
    }

    private String processMessage(String message) {
        message = applyAutoTrimWhitespace(message); // Apply trimming
        message = applyCharacterReplacement(message);
        message = applyTextWrapping(message);
        return message;
    }

    @SimpleFunction
    public void Component(Texting texting){
      this.texting = texting;
    }

    // Property 1: Default Prefix
    @SimpleProperty(description = "Sets the default prefix for all messages.")
    public void SetDefaultPrefix(String prefix) {
        if (prefix == null) throw new IllegalArgumentException("Prefix cannot be null.");
        this.defaultPrefix = prefix;
    }

    @SimpleProperty(description = "Gets the default prefix for all messages.")
    public String DefaultPrefix() {
        return defaultPrefix;
    }

    // Property 2: Default Suffix
    @SimpleProperty(description = "Sets the default suffix for all messages.")
    public void SetDefaultSuffix(String suffix) {
        if (suffix == null) throw new IllegalArgumentException("Suffix cannot be null.");
        this.defaultSuffix = suffix;
    }

    @SimpleProperty(description = "Gets the default suffix for all messages.")
    public String DefaultSuffix() {
        return defaultSuffix;
    }

    // Property 4: Custom Delimiter
    @SimpleProperty(description = "Sets a custom delimiter for spaces in messages.")
    public void SetCustomDelimiter(String delimiter) {
        if (delimiter == null) throw new IllegalArgumentException("Delimiter cannot be null.");
        this.customDelimiter = delimiter;
    }

    @SimpleProperty(description = "Gets the custom delimiter used for spaces in messages.")
    public String CustomDelimiter() {
        return customDelimiter;
    }

    // Property 5: Max Message Length
    @SimpleProperty(description = "Sets the maximum allowed length for a message.")
    public void SetMaxMessageLength(int length) {
        if (length <= 0) throw new IllegalArgumentException("Message length must be greater than zero.");
        this.maxMessageLength = length;
    }

    @SimpleProperty(description = "Gets the maximum allowed length for a message.")
    public int MaxMessageLength() {
        return maxMessageLength;
    }

    // Property 6: Character Replacement
    @SimpleProperty(description = "Sets a character replacement rule.")
    public void SetCharacterReplacement(String replacement) {
        if (replacement == null)
            throw new IllegalArgumentException("Replacement cannot be null.");
        this.characterReplacementValue = replacement;
    }

    @SimpleProperty(description = "Gets the replacement character.")
    public String CharacterReplacementValue() {
        return characterReplacementValue;
    }

    private String applyCharacterReplacement(String message) {
        if (!characterReplacementValue.isEmpty()) {
            return message.replace(characterReplacementTarget, characterReplacementValue);
        }
        return message;
    }

    // Property 7: Text Wrapping
    @SimpleProperty(description = "Enables or disables text wrapping.")
    public void SetTextWrappingEnabled(boolean enable) {
        this.textWrappingEnabled = enable;
    }

    @SimpleProperty(description = "Checks if text wrapping is enabled.")
    public boolean TextWrappingEnabled() {
        return textWrappingEnabled;
    }

    private String applyTextWrapping(String message) {
        if (textWrappingEnabled) {
            return message.replaceAll("(.{1,50})(\\s+|$)", "$1\n");
        }
        return message;
    }

    // Function: Send Message (Synchronous Version)
    @SimpleFunction(description = "Sends a processed message to the set phone number synchronously.")
    public void SendMessage(String message) {
       if (texting != null) {
         String processedMessage = processMessage(message);
        
         // Set the message and phone number before sending
         texting.Message(processedMessage);
        
         // Send the message synchronously
         texting.SendMessage();
       } else {
        SendError("Texting component not set.");
       }
   }


    @SimpleEvent(description = "Triggered when a message is successfully sent.")
    public void SendSuccess(String successMessage) {
        EventDispatcher.dispatchEvent(this, "SendSuccess", successMessage);
    }

    @SimpleEvent(description = "Triggered when an error occurs during message sending.")
    public void SendError(String errorMessage) {
        EventDispatcher.dispatchEvent(this, "SendError", errorMessage);
    }

    @SimpleEvent(description = "Triggered when there is no internet connection.")
    public void NoInternetConnection() {
        EventDispatcher.dispatchEvent(this, "NoInternetConnection");
    }

    // Set the phone number for the Texting component
    @SimpleFunction(description = "Sets the phone number for the Texting component.")
    public void SetPhoneNumber(String phoneNumber) {
        if (texting != null) {
            texting.PhoneNumber(phoneNumber); // Set the phone number
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm != null) {
            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            return activeNetwork != null && activeNetwork.isConnected();
        }
        return false;
    }

    private String applyAutoTrimWhitespace(String message) {
        if (autoTrimWhitespace) {
            return message.trim();
        }
        return message;
    }
}
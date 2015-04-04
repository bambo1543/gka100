package it.bambo.gka100;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.telephony.SmsManager;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import it.bambo.gka100.attributes.AudioAttributes;
import it.bambo.gka100.utils.StringUtils;

/**
 *
 * @author <a href="mailto:andreas.bga@gmail.com">Andreas Baumgartner</a> on 23.03.14.
 */
public class SettingsActivity extends PreferenceActivity {

    private final static SimpleDateFormat DATE_TIME_FORMAT = new SimpleDateFormat("HH mm dd MM yy");

    private SmsManager smsManager;

    private String phoneNumber;
    private String phonePin;

    private EditTextPreference phonePinPreference;
    private EditTextPreference changePhonePinPreference;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
        smsManager = SmsManager.getDefault();

        phonePinPreference = (EditTextPreference) findPreference(getString(R.string.phonePinKey));
        assert phonePinPreference != null;
        phonePinPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object o) {
                SharedPreferences.Editor editor = getPreferenceManager().getSharedPreferences().edit();
                String newPin = o.toString();
                editor.putString("changePhonePin", newPin).apply();
                changePhonePinPreference.setText(newPin);
                return true;
            }
        });

        changePhonePinPreference = (EditTextPreference) findPreference(getString(R.string.changePhonePinKey));
        assert changePhonePinPreference != null;
        changePhonePinPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object o) {
                String newPin = o.toString();
                sendAction(preference.getContext(), "SET PIN " + newPin);
                SharedPreferences.Editor editor = getPreferenceManager().getSharedPreferences().edit();
                editor.putString("phonePin", newPin).apply();
                phonePinPreference.setText(newPin);
                return true;
            }
        });

        EditTextPreference alarmDeviceName = (EditTextPreference) findPreference(getString(R.string.alarmDeviceNameKey));
        assert alarmDeviceName != null;
        alarmDeviceName.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object o) {
                sendAction(preference.getContext(), "SET NAME " + o.toString());
                return true;
            }
        });

        Preference setTimeDateButton = findPreference(getString(R.string.setTimeDateButton));
        assert setTimeDateButton != null;
        setTimeDateButton.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Date now = new Date();
                sendAction(preference.getContext(), "SET DATE " + DATE_TIME_FORMAT.format(now));
                return true;
            }
        });

        initPhoneNumbers();

        initAudio();

    }

    private void initPhoneNumbers() {
        EditTextPreference phoneNumber1 = (EditTextPreference) findPreference(getString(R.string.phoneNumber1Key));
        assert phoneNumber1 != null;
        phoneNumber1.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object o) {
                sendAction(preference.getContext(), "SET TEL1 " + o.toString());
                return true;
            }
        });
        EditTextPreference phoneNumber2 = (EditTextPreference) findPreference(getString(R.string.phoneNumber2Key));
        assert phoneNumber2 != null;
        phoneNumber2.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object o) {
                sendAction(preference.getContext(), "SET TEL2 " + o.toString());
                return true;
            }
        });
        EditTextPreference phoneNumber3 = (EditTextPreference) findPreference(getString(R.string.phoneNumber3Key));
        assert phoneNumber3 != null;
        phoneNumber3.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object o) {
                sendAction(preference.getContext(), "SET TEL3 " + o.toString());
                return true;
            }
        });
        EditTextPreference phoneNumber4 = (EditTextPreference) findPreference(getString(R.string.phoneNumber4Key));
        assert phoneNumber4 != null;
        phoneNumber4.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object o) {
                sendAction(preference.getContext(), "SET TEL4 " + o.toString());
                return true;
            }
        });
        EditTextPreference phoneNumber5 = (EditTextPreference) findPreference(getString(R.string.phoneNumber5Key));
        assert phoneNumber5 != null;
        phoneNumber5.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object o) {
                sendAction(preference.getContext(), "SET TEL5 " + o.toString());
                return true;
            }
        });
        EditTextPreference phoneNumber6 = (EditTextPreference) findPreference(getString(R.string.phoneNumber6Key));
        assert phoneNumber6 != null;
        phoneNumber6.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object o) {
                sendAction(preference.getContext(), "SET TEL6 " + o.toString());
                return true;
            }
        });
    }

    private void initAudio() {
        Preference sendAudioSettingsButton = findPreference(getString(R.string.sendAudioSettingsButton));
        assert sendAudioSettingsButton != null;
        sendAudioSettingsButton.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                SharedPreferences sharedPreferences = getPreferenceManager().getSharedPreferences();
                assert sharedPreferences != null;

                StringBuilder sb = new StringBuilder("SET AUDIO ");
                sb.append(sharedPreferences.getString(AudioAttributes.SPEAKER.getPreferenceKey(), AudioAttributes.SPEAKER.getDefaultValue())).append(" ");
                sb.append(sharedPreferences.getString(AudioAttributes.MICROPHONE.getPreferenceKey(), AudioAttributes.MICROPHONE.getDefaultValue())).append(" ");
                sb.append(sharedPreferences.getString(AudioAttributes.RINGTONE_MELODY.getPreferenceKey(), AudioAttributes.RINGTONE_MELODY.getDefaultValue())).append(" ");
                sb.append(sharedPreferences.getString(AudioAttributes.RINGTONE_VOLUME.getPreferenceKey(), AudioAttributes.RINGTONE_VOLUME.getDefaultValue())).append(" ");
                sb.append(sharedPreferences.getString(AudioAttributes.ALARM_VOLUME.getPreferenceKey(), AudioAttributes.ALARM_VOLUME.getDefaultValue())).append(" ");
                sb.append(sharedPreferences.getString(AudioAttributes.CONFIRM_VOLUME.getPreferenceKey(), AudioAttributes.CONFIRM_VOLUME.getDefaultValue()));

                sendAction(preference.getContext(), sb.toString());
                return true;
            }
        });
        Preference receiveAudioSettingsButton = findPreference(getString(R.string.receiveAudioSettingsButton));
        assert receiveAudioSettingsButton != null;
        receiveAudioSettingsButton.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                sendAction(preference.getContext(), "TEST AUDIO");
                return true;
            }
        });
        Preference resetAudioSettingsButton = findPreference(getString(R.string.resetAudioSettingsButton));
        assert resetAudioSettingsButton != null;
        resetAudioSettingsButton.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                sendAction(preference.getContext(), "RESET AUDIO");
                return true;
            }
        });
    }

    private void sendAction(Context context, String action) {
        try {
            updatePhoneNumberAndPin();
            smsManager.sendTextMessage(phoneNumber, null, action + " #" + phonePin, null, null);
            Toast.makeText(context, "Sent Action: " + action, Toast.LENGTH_LONG).show();
        } catch (ParseException e) {
            Toast.makeText(context, "Unable to parse: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void updatePhoneNumberAndPin() throws ParseException {
        phoneNumber = getPreferenceManager().getSharedPreferences().getString("phoneNumber", null);
        if(StringUtils.isEmpty(phoneNumber)) throw new ParseException("phoneNumber", 0);

        phonePin = getPreferenceManager().getSharedPreferences().getString("phonePin", "1513");
        if(StringUtils.isEmpty(phonePin)) throw new ParseException("phonePin", 0);
    }



}

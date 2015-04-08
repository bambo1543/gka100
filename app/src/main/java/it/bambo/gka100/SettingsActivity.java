package it.bambo.gka100;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceGroup;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import it.bambo.gka100.attributes.AudioAttributes;
import it.bambo.gka100.sms.SMSSender;

/**
 *
 * @author <a href="mailto:andreas.bga@gmail.com">Andreas Baumgartner</a> on 23.03.14.
 */
public class SettingsActivity extends PreferenceActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    private final static SimpleDateFormat DATE_TIME_FORMAT = new SimpleDateFormat("HH mm dd MM yy");

    private SMSSender smsSender;

    private EditTextPreference phonePinPreference;
    private EditTextPreference changePhonePinPreference;

    private List<String> excludeValueAsSummary = Arrays.asList("changePhonePin", "resetOneNumber");

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences_screen);
        smsSender = SMSSender.getInstance();

        initBasics();
        initPhoneNumbers();
        initAudio();

        EditTextPreference voltage = (EditTextPreference) findPreference(getString(R.string.minVoltageKey));
        assert voltage != null;
        voltage.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object o) {
                smsSender.sendAction(preference.getContext(), "SET VOLTAGE " + o.toString());
                return false;
            }
        });
        Preference receiveVoltage = findPreference(getString(R.string.receiveVoltageKey));
        assert receiveVoltage != null;
        receiveVoltage.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                smsSender.sendAction(preference.getContext(), "TEST VOLTAGE");
                return false;
            }
        });
        Preference resetVoltage = findPreference(getString(R.string.resetVoltageKey));
        assert resetVoltage != null;
        resetVoltage.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                smsSender.sendAction(preference.getContext(), "RESET VOLTAGE");
                return false;
            }
        });


        initSummary(getPreferenceScreen());
    }

    private void initBasics() {
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
                smsSender.sendAction(preference.getContext(), "SET PIN " + newPin);
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
                smsSender.sendAction(preference.getContext(), "SET NAME " + o.toString());
                return true;
            }
        });

        Preference setTimeDateButton = findPreference(getString(R.string.setTimeDateKey));
        assert setTimeDateButton != null;
        setTimeDateButton.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Date now = new Date();
                smsSender.sendAction(preference.getContext(), "SET DATE " + DATE_TIME_FORMAT.format(now));
                return true;
            }
        });
    }

    private void initPhoneNumbers() {
        EditTextPreference phoneNumber1 = (EditTextPreference) findPreference(getString(R.string.phoneNumber1Key));
        assert phoneNumber1 != null;
        phoneNumber1.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object o) {
                smsSender.sendAction(preference.getContext(), "SET TEL1 " + o.toString());
                return false;
            }
        });
        EditTextPreference phoneNumber2 = (EditTextPreference) findPreference(getString(R.string.phoneNumber2Key));
        assert phoneNumber2 != null;
        phoneNumber2.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object o) {
                smsSender.sendAction(preference.getContext(), "SET TEL2 " + o.toString());
                return false;
            }
        });
        EditTextPreference phoneNumber3 = (EditTextPreference) findPreference(getString(R.string.phoneNumber3Key));
        assert phoneNumber3 != null;
        phoneNumber3.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object o) {
                smsSender.sendAction(preference.getContext(), "SET TEL3 " + o.toString());
                return false;
            }
        });
        EditTextPreference phoneNumber4 = (EditTextPreference) findPreference(getString(R.string.phoneNumber4Key));
        assert phoneNumber4 != null;
        phoneNumber4.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object o) {
                smsSender.sendAction(preference.getContext(), "SET TEL4 " + o.toString());
                return false;
            }
        });
        EditTextPreference phoneNumber5 = (EditTextPreference) findPreference(getString(R.string.phoneNumber5Key));
        assert phoneNumber5 != null;
        phoneNumber5.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object o) {
                smsSender.sendAction(preference.getContext(), "SET TEL5 " + o.toString());
                return false;
            }
        });
        EditTextPreference phoneNumber6 = (EditTextPreference) findPreference(getString(R.string.phoneNumber6Key));
        assert phoneNumber6 != null;
        phoneNumber6.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object o) {
                smsSender.sendAction(preference.getContext(), "SET TEL6 " + o.toString());
                return false;
            }
        });

        Preference readoutAllNumbers = findPreference(getString(R.string.readoutAllNumbersKey));
        assert readoutAllNumbers != null;
        readoutAllNumbers.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                smsSender.sendAction(preference.getContext(), "TEST TEL");
                return true;
            }
        });
        Preference resetAllNumbers = findPreference(getString(R.string.resetAllNumbersKey));
        assert resetAllNumbers != null;
        resetAllNumbers.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                smsSender.sendAction(preference.getContext(), "RESET TELALL");
                return true;
            }
        });
        EditTextPreference resetOneNumber = (EditTextPreference) findPreference(getString(R.string.resetOneNumberKey));
        assert resetOneNumber != null;
        resetOneNumber.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object o) {
                smsSender.sendAction(preference.getContext(), "RESET TEL" + o.toString());
                return false;
            }
        });

    }

    private void initAudio() {
        Preference sendAudioSettingsButton = findPreference(getString(R.string.sendAudioSettingsKey));
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

                smsSender.sendAction(preference.getContext(), sb.toString());
                return true;
            }
        });
        Preference receiveAudioSettingsButton = findPreference(getString(R.string.receiveAudioSettingsKey));
        assert receiveAudioSettingsButton != null;
        receiveAudioSettingsButton.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                smsSender.sendAction(preference.getContext(), "TEST AUDIO");
                return true;
            }
        });
        Preference resetAudioSettingsButton = findPreference(getString(R.string.resetAudioSettingsKey));
        assert resetAudioSettingsButton != null;
        resetAudioSettingsButton.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                smsSender.sendAction(preference.getContext(), "RESET AUDIO");
                return true;
            }
        });
    }

    private void initSummary(Preference p) {
        if (p instanceof PreferenceGroup) {
            PreferenceGroup pGrp = (PreferenceGroup) p;
            for (int i = 0; i < pGrp.getPreferenceCount(); i++) {
                initSummary(pGrp.getPreference(i));
            }
        } else {
            updatePreference(p.getKey());
        }
    }

    private void updatePreference(String key) {
        Preference p = findPreference(key);
        SharedPreferences preferences = p.getSharedPreferences();

        if (p instanceof EditTextPreference) {
            EditTextPreference editTextPreference = (EditTextPreference) p;
            Object value = preferences.getAll().get(key);
            String string = value == null ? "" : value.toString();
            editTextPreference.setText(string);
            if(!excludeValueAsSummary.contains(key))
                editTextPreference.setSummary(string);
        } else if (p instanceof ListPreference) {
            ListPreference listPref = (ListPreference) p;
            listPref.setValueIndex(Integer.valueOf((String) preferences.getAll().get(key)));
            if(!excludeValueAsSummary.contains(key))
                p.setSummary(listPref.getEntry());
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Set up a listener whenever a key changes
        getPreferenceScreen().getSharedPreferences()
                .registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Unregister the listener whenever a key changes
        getPreferenceScreen().getSharedPreferences()
                .unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences preferences, String key) {
        updatePreference(key);
    }
}

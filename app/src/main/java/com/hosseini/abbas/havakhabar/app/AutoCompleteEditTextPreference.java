package com.hosseini.abbas.havakhabar.app;

import android.content.Context;
import android.preference.EditTextPreference;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;

public class AutoCompleteEditTextPreference extends EditTextPreference
{
    public AutoCompleteEditTextPreference(Context context)
    {
        super(context);
    }

    public AutoCompleteEditTextPreference(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public AutoCompleteEditTextPreference(Context context, AttributeSet attrs,
                                          int defStyle)
    {
        super(context, attrs, defStyle);
    }

    /**
     * the default EditTextPreference does not make it easy to
     * use an AutoCompleteEditTextPreference field. By overriding this method
     * we perform surgery on it to use the type of edit field that
     * we want.
     */
    protected void onBindDialogView(View view)
    {
        super.onBindDialogView(view);

        // find the current EditText object
        final EditText editText = (EditText)view.findViewById(android.R.id.edit);
        // copy its layout params
        ViewGroup.LayoutParams params = editText.getLayoutParams();
        ViewGroup vg = (ViewGroup)editText.getParent();
        String curVal = editText.getText().toString();
        // remove it from the existing layout hierarchy
        vg.removeView(editText);
        // construct a new editable autocomplete object with the appropriate params
        // and id that the TextEditPreference is expecting
        mACTV = new AutoCompleteTextView(getContext());
        mACTV.setLayoutParams(params);
        mACTV.setId(android.R.id.edit);
        mACTV.setText(curVal);


        String[] CountryList = getContext().getResources().getStringArray(R.array.list_of_countries);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_dropdown_item_1line, CountryList);
        mACTV.setAdapter(adapter);

        // add the new view to the layout
        vg.addView(mACTV);
    }

    /**
     * Because the baseclass does not handle this correctly
     * we need to query our injected AutoCompleteTextView for
     * the value to save 
     */
    protected void onDialogClosed(boolean positiveResult)
    {
        super.onDialogClosed(positiveResult);

        if (positiveResult && mACTV != null)
        {
            String value = mACTV.getText().toString();
            if (callChangeListener(value)) {
                setText(value);
            }
        }
    }

    /**
     * again we need to override methods from the base class
     */
    public EditText getEditText()
    {
        return mACTV;
    }

    private AutoCompleteTextView mACTV = null;
    private final String TAG = "AutoCompleteEditTextPreference";
}
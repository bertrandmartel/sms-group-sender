/**
 * The MIT License (MIT)
 * <p/>
 * Copyright (c) 2017 Bertrand Martel
 * <p/>
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * <p/>
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * <p/>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package fr.bmartel.groupsms.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Contact model.
 *
 * @author Bertrand Martel
 */
public class Contact {

    @SerializedName("displayName")
    private String mDisplayName;

    @SerializedName("phoneNumberSpinner")
    private List<String> mPhoneNumber;

    @SerializedName("selectedPhoneNumber")
    private String mSelectedPhoneNumber;

    @SerializedName("checked")
    private boolean mChecked;

    public Contact(String displayName, List<String> phoneNumber, boolean check, String selectedPhoneNumber) {
        mDisplayName = displayName;
        mPhoneNumber = phoneNumber;
        mChecked = check;
        mSelectedPhoneNumber = selectedPhoneNumber;
    }

    public String getDisplayName() {
        return mDisplayName;
    }

    public List<String> getPhoneNumber() {
        return mPhoneNumber;
    }

    public boolean isChecked() {
        return mChecked;
    }

    public void setChecked(boolean checked) {
        this.mChecked = checked;
    }

    public String getSelectedPhoneNumber() {
        return mSelectedPhoneNumber;
    }

    public void setSelectedPhoneNumber(String selectedPhoneNumber) {
        mSelectedPhoneNumber = selectedPhoneNumber;
    }
}

/* Copyright (c) 2012 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.seastargames.android.sdk.iabutil;

import java.io.Serializable;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Represents an in-app billing purchase.
 */
public class Purchase implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 8865157311086891281L;
	String mItemType;  // ITEM_TYPE_INAPP or ITEM_TYPE_SUBS
    String mOrderId;
    String mPackageName;
    String mSku;
    long mPurchaseTime;
    int mPurchaseState;
    String mDeveloperPayload;
    String mToken;
    String mOriginalJson;
    String mSignature;
    
    String mGName;
    String uID;
    String source;
    String cparam;
    String currency;
    String serverId;
    
    public Purchase() {
    }

    public Purchase(String itemType, String jsonPurchaseInfo, String signature) throws JSONException {
        mItemType = itemType;
        mOriginalJson = jsonPurchaseInfo;
        JSONObject o = new JSONObject(mOriginalJson);
        mOrderId = o.optString("orderId");
        mPackageName = o.optString("packageName");
        mSku = o.optString("productId");
        mPurchaseTime = o.optLong("purchaseTime");
        mPurchaseState = o.optInt("purchaseState");
        mDeveloperPayload = o.optString("developerPayload");
        mToken = o.optString("token", o.optString("purchaseToken"));
        mSignature = signature;
    }
    
    public Purchase(Purchase purchase) {
    	this.mItemType = purchase.getItemType();
        this.mOrderId = purchase.getOrderId();
        this.mPackageName = purchase.getPackageName();
        this.mSku = purchase.getSku();
        this.mPurchaseTime = purchase.getPurchaseTime();
        this.mPurchaseState = purchase.getPurchaseState();
        this.mDeveloperPayload = purchase.getDeveloperPayload();
        this.mToken = purchase.getToken();
        this.mOriginalJson = purchase.getOriginalJson();
        this.mSignature = purchase.getSignature();
        
        this.mGName = purchase.getmGName();
        this.uID = purchase.getUID();
        this.source = purchase.getSource();
        this.cparam = purchase.getCparam();
        this.currency = purchase.getCurrency();
        this.serverId = purchase.getServerId();
    }
    
    public Purchase(String mGName, String gameServerId, String source, String gameRoleId, String productId, String currency, String extraData) {
        this.mGName = mGName;
        this.serverId = gameServerId;
        this.source = source;
        this.uID = gameRoleId;
        this.mSku = productId;
        this.currency = currency;
        this.cparam = extraData;
    }

    public String getItemType() { return mItemType; }
    public String getOrderId() { return mOrderId; }
    public String getPackageName() { return mPackageName; }
    public String getSku() { return mSku; }
    public long getPurchaseTime() { return mPurchaseTime; }
    public int getPurchaseState() { return mPurchaseState; }
    public String getDeveloperPayload() { return mDeveloperPayload; }
    public String getToken() { return mToken; }
    public String getOriginalJson() { return mOriginalJson; }
    public String getSignature() { return mSignature; }
    public String getServerId() { return serverId; }
    
    public String getmGName() {
		return mGName;
	}

	public void setmGName(String mGName) {
		this.mGName = mGName;
	}

	public String getUID() {
		return uID;
	}

	public void setUID(String uID) {
		this.uID = uID;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getCparam() {
		return cparam;
	}

	public void setCparam(String cparam) {
		this.cparam = cparam;
	}
	
	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}
	
	public void setServerId(String serverId) {
		this.serverId = serverId;
	}
	
	public void setSku(String sku) {
		this.mSku = sku;
	}

	@Override
    public String toString() { return "PurchaseInfo(type:" + mItemType + "):" + mOriginalJson; }
}

package com.seastargames.android.sdk;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;

import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.Player;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.PlusShare;
import com.google.android.gms.plus.model.people.Person;
import com.seastargames.android.google.pay.IabHelper;
import com.seastargames.android.google.pay.IabResult;
import com.seastargames.android.google.pay.Inventory;
import com.seastargames.android.google.pay.Purchase;
import com.seastargames.android.google.pay.SkuDetails;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

public class Google {

	private static final int REQUEST_CODE_SIGN_IN = 30000;
	private static final int REQUEST_CODE_ERROR_DIALOG = 30001;
	private static final int REQUEST_CODE_INTERACTIVE_POST = 30002;
	private static final int REQUEST_CODE_PAY = 10001;
	private static final int REQUEST_CODE_GAMES = 5001;
	private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 1000;
	private static final String TAG = "SeaStar";

	public interface OnLoginCallBack {
		void onLoginCallBack(boolean success, GoogleUserModel googleUserModel);
	}

	public interface OnInitCallBack {
		void onInitCallBack(boolean success);
	}

	public interface OnQueryInventoryCallBack {
		void onQueryInventoryCallBack(boolean success,
				List<GoogleSkuModel> googleSkuModel);
	}

	public class GoogleUserModel {

		public String id;
		public String aboutMe;
		public String ageRange;
		public String birthday;
		public String braggingRights;
		public String cover;
		public String currentLocation;
		public String displayName;
		public String image;
		public String language;
		public String name;
		public String nickName;
		public String organizations;
		public String plusOneCount;
		public String url;

	}

	public interface OnQuerySkusDetailsCallBack {
		void onQuerySkusDetailsCallBack(boolean success,
				List<GoogleSkuModel> googleSkuModel);
	}

	public interface OnPayCallBack {
		void onPayCallBack(boolean success, String sku, String itemType,
				String googleOrder, String signature,
				String purchaseOriginalData);
	}

	public interface OnConsumeCallBack {
		void onConsumeCallBack(boolean success, String sku);
	}

	public interface OnLinkShareCallBack {
		void onLinkShareCallBack(boolean success);
	}

	public interface OnPhotoShareCallBack {
		void onPhotoShareCallBack(boolean success);
	}

	public class GoogleSkuModel {
		public String description; // 商品描述
		public String price; // 商品显示价格
		public String sku; // 商品ID
		public String title; // 商品名称
		public String type; // 商品 类型 （可消费） （月卡）
		public String priceAmountMicros; // 商品实际价格
		public String priceCurrencyCode; // 商品价格单位
		public String googleOrder; // 谷歌订单号
		public String signature; // 谷歌签名
		public String purchaseOriginalData; // 谷歌交易详细信息
		public String itemType;

	}

	private enum ShareType {
		LINK, PHOTO
	}

	private IabHelper helper;
	private GoogleApiClient googleApiClient;

	private OnLoginCallBack onLoginCallBack;
	private OnLinkShareCallBack onLinkShareCallBack;
	private OnPhotoShareCallBack onPhotoShareCallBack;
	private ShareType shareType;
	private Activity activity = null;

	private GoogleApiClient.ConnectionCallbacks ConnectionCb = new GoogleApiClient.ConnectionCallbacks() {

		@Override
		public void onConnectionSuspended(int arg0) {
			Log.d(TAG, "Google::ConnectionCb::onConnectionSuspended 重新登录");
			if (googleApiClient != null)
				googleApiClient.connect();
		}

		@Override
		public void onConnected(Bundle arg0) {
			// TODO Auto-generated method stub

			Player p = Games.Players.getCurrentPlayer(googleApiClient);

			Person person = Plus.PeopleApi.getCurrentPerson(googleApiClient);
			if (person != null) {
				Log.d(TAG, "Google::ConnectionCb::onConnected 获取账号信息成功，登陆成功");
				GoogleUserModel googleUserModel = new GoogleUserModel();

				if (person.hasAboutMe()) {
					googleUserModel.aboutMe = person.getAboutMe();
				} else {
					googleUserModel.aboutMe = "";
				}
				if (person.hasAgeRange()) {
					googleUserModel.ageRange = person.getAgeRange().toString();
				} else {
					googleUserModel.ageRange = "";
				}
				if (person.hasBirthday()) {
					googleUserModel.birthday = person.getBirthday();
				} else {
					googleUserModel.birthday = "";
				}
				if (person.hasBraggingRights()) {
					googleUserModel.braggingRights = person.getBraggingRights();
				} else {
					googleUserModel.braggingRights = "";
				}
				if (person.hasCover()) {
					googleUserModel.cover = person.getCover().toString();
				} else {
					googleUserModel.cover = "";
				}
				if (person.hasCurrentLocation()) {
					googleUserModel.currentLocation = person
							.getCurrentLocation();
				} else {
					googleUserModel.currentLocation = "";
				}
				if (person.hasDisplayName()) {
					googleUserModel.displayName = person.getDisplayName();
				} else {
					googleUserModel.displayName = "";
				}
				if (person.hasId()) {
					googleUserModel.id = person.getId();
				} else {
					googleUserModel.id = "";
				}
				if (person.hasImage()) {
					googleUserModel.image = person.getImage().toString();
				} else {
					googleUserModel.image = "";
				}
				if (person.hasLanguage()) {
					googleUserModel.language = person.getLanguage();
				} else {
					googleUserModel.language = "";
				}
				if (person.hasName()) {
					googleUserModel.name = person.getName().toString();
				} else {
					googleUserModel.name = "";
				}
				if (person.hasNickname()) {
					googleUserModel.nickName = person.getNickname();
				} else {
					googleUserModel.nickName = "";
				}
				if (person.hasOrganizations()) {
					googleUserModel.organizations = person.getOrganizations()
							.toString();
				} else {
					googleUserModel.organizations = "";
				}
				if (person.hasUrl()) {
					googleUserModel.url = person.getUrl();
				} else {
					googleUserModel.url = "";
				}
				onLoginCallBack.onLoginCallBack(true, googleUserModel);

			} else {
				Log.d(TAG, "Google::ConnectionCb::onConnected 获取账号信息失败，登陆失败");
				onLoginCallBack.onLoginCallBack(false, null);
			}

			onLoginCallBack = null;
		}
	};

	private GoogleApiClient.OnConnectionFailedListener ConnectionFailCb = new GoogleApiClient.OnConnectionFailedListener() {

		public void onConnectionFailed(ConnectionResult result) {
			// TODO Auto-generated method stub

			if (result.hasResolution()) {
				try {
					// 请求登录
					Log.d(TAG,
							"Google::ConnectionFailCb::onConnectionFailed 登录失败，再进行登陆");
					result.startResolutionForResult(activity,
							REQUEST_CODE_SIGN_IN);

				} catch (IntentSender.SendIntentException e) {
					e.printStackTrace();
					googleApiClient.connect();
				}
			} else {
				// 登录失败
				GoogleApiAvailability.getInstance().showErrorDialogFragment(
						activity, result.getErrorCode(),
						REQUEST_CODE_ERROR_DIALOG);
				onLoginCallBack.onLoginCallBack(false, null);
				onLoginCallBack = null;
			}
		}
	};

	public void setCurActivity(Activity activity) {
		this.activity = activity;
	}

	public void init(final OnInitCallBack oick) {

		try {
			// 授权
			GoogleApiClient.Builder builder = new GoogleApiClient.Builder(
					activity).addConnectionCallbacks(ConnectionCb)
					.addOnConnectionFailedListener(ConnectionFailCb)
					.addApi(Games.API).addScope(Games.SCOPE_GAMES)
					.addApi(Plus.API).addScope(Plus.SCOPE_PLUS_PROFILE);
			googleApiClient = builder.build();

			// 支付
			helper = new IabHelper(activity, "");

			helper.enableDebugLogging(false);

			if (!checkPlayServices(activity)) {
				oick.onInitCallBack(false);
				return;
			}

			helper.startSetup(new IabHelper.OnIabSetupFinishedListener() {

				@Override
				public void onIabSetupFinished(IabResult result) {
					if (result.isSuccess()) {
						oick.onInitCallBack(true);
					} else {
						oick.onInitCallBack(false);
					}
				}
			});
		} catch (Exception e) {
			Log.d(TAG, "谷歌支付系统初始化失败，请检查如下情况：");
			Log.d(TAG, "1. 请检查手机上是否有谷歌框架（可以查看是否有谷歌商店）");
			Log.d(TAG, "2. 请检查手机上是否有谷歌账号（可以进入谷歌商店看是否有账号）");
			Log.d(TAG, "3. 请检查谷歌账号是否能够支付（可以进入谷歌商店看是否有能花钱购买的app）");
			Log.d(TAG, "4. 请检查谷歌账号是否是测试账号（去谷歌后台查看）");
			Log.d(TAG, "5. 请检查谷歌账号是接收了测试邀请（去谷歌后台获取测试邀请链接打开）");
			Log.d(TAG, "6. 请检查APK签名是否与提交给谷歌的APK相同");
			Log.d(TAG, "7. 请检查APK VersionCode是否与提交给谷歌的APK相同");
			e.printStackTrace();
		}

	}

	public void login(OnLoginCallBack onLoginCallBack) {

		this.onLoginCallBack = onLoginCallBack;

		if (googleApiClient != null) {
			if (googleApiClient.isConnected()) {
				Log.d(TAG, "Google::login 已经授权过，清除授权，重新开始");
				// 已经授权过了，需要解除授权
				logout();
				googleApiClient.connect();
			} else if (googleApiClient.isConnecting()) {
				// 正在授权中，什么都不做
				Log.d(TAG, "Google::login 正在授权，请稍后。。。");
			} else {
				Log.d(TAG, "Google::login 没有授权过，开始授权");
				googleApiClient.connect();
			}
		} else {
			Log.d(TAG, "Google::login 授权对象不存在，授权失败");
			this.onLoginCallBack.onLoginCallBack(false, null);
			this.onLoginCallBack = null;
		}

	}

	public void logout() {

		if (googleApiClient != null && googleApiClient.isConnected()) {
			Games.signOut(googleApiClient);
			googleApiClient.clearDefaultAccountAndReconnect();
			googleApiClient.disconnect();
		}
	}

	// 解锁成就
	public void unlockAchievement(String achievementId) {
		Games.Achievements.unlock(googleApiClient, achievementId);
	}

	// 添加成就
	public void incrementAchievement(String achievementId, int score) {
		Log.d(TAG, "googleApiClient" + googleApiClient.isConnected());
		Games.Achievements.increment(googleApiClient, achievementId, score);
	}

	// 展示成就
	public void showAchievement() {
		activity.startActivityForResult(
				Games.Achievements.getAchievementsIntent(googleApiClient),
				REQUEST_CODE_GAMES);
	}

	// 排行榜加分
	public void updateScoreOnLeaderboard(String leaderboardId, int score) {
		Games.Leaderboards.submitScore(googleApiClient, leaderboardId, score);
	}

	// 展示排行榜
	public void showLeaderboard() {
		activity.startActivityForResult(
				Games.Leaderboards.getAllLeaderboardsIntent(googleApiClient),
				REQUEST_CODE_GAMES);
	}

	public void shareLink(String contentUrl, String desc,
			OnLinkShareCallBack listener) {

		try {
			if (GooglePlayServicesUtil.isGooglePlayServicesAvailable(activity) == ConnectionResult.SUCCESS) {
				onLinkShareCallBack = listener;
				shareType = ShareType.LINK;

				// Create an interactive post builder.
				PlusShare.Builder builder = new PlusShare.Builder(activity);

				// Set the target url (for desktop use).
				builder.setContentUrl(Uri.parse(contentUrl));

				// Set the target deep-link ID (for mobile use).
				builder.setContentDeepLinkId(contentUrl, null, null, null);

				// Set the pre-filled message.
				builder.setText(desc);
				builder.setType("text/plain");

				activity.startActivityForResult(builder.getIntent(),
						REQUEST_CODE_INTERACTIVE_POST);
			} else {
				Log.d(TAG, "Google服务无法使用");
			}
		} catch (Exception e) {
			e.printStackTrace();

		}
	}

	// 只能分享本地图片
	// 必须安装Google+
	public void sharePhoto(Activity activity, String imageUri, String desc,
			OnPhotoShareCallBack listener) {
		try {
			if (GooglePlayServicesUtil.isGooglePlayServicesAvailable(activity) == ConnectionResult.SUCCESS) {
				onPhotoShareCallBack = listener;
				shareType = ShareType.PHOTO;

				File file = new File(imageUri);
				Uri ur = Uri.fromFile(file);
				PlusShare.Builder share = new PlusShare.Builder(activity)
						.setText(desc).addStream(ur).setType("image/*");
				Intent intent = share.getIntent();
				activity.startActivityForResult(intent,
						REQUEST_CODE_INTERACTIVE_POST);
			} else {
				Log.d(TAG, "Google服务无法使用");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (requestCode == REQUEST_CODE_SIGN_IN) {
			// Previous resolution intent no longer in progress.
			Log.d(TAG, "Google::onActivityResult reqeuest code sign in");

			if (resultCode == Activity.RESULT_OK) {
				// After resolving a recoverable error, now retry connect().
				// Note that it's possible
				// mGoogleApiClient is already connected or connecting due to
				// rotation / Activity
				// restart while user is walking through the (possibly full
				// screen) resolution
				// Activities. We should always reconnect() and ignore earlier
				// connection attempts
				// started before completion of the resolution. (With only one
				// exception, a
				// connect() attempt started late enough in the resolution flow
				// and it actually
				// succeeded)
				Log.d(TAG, "Google::onActivityResult reqeuest result ok");
				if (!googleApiClient.isConnected()) {
					Log.d(TAG,
							"Google::onActivityResult reqeuest result ok reconnect");
					googleApiClient.reconnect();
				}
			} else {
				// No longer in the middle of resolving sign-in errors.
				Log.d(TAG, "Google::onActivityResult reqeuest result error");
				onLoginCallBack.onLoginCallBack(false, null);
				onLoginCallBack = null;

			}
		} else if (requestCode == REQUEST_CODE_INTERACTIVE_POST) {
			if (resultCode != Activity.RESULT_OK) {
				Log.d(TAG, "Google::onActivityResult 分享失败");
				if (shareType == ShareType.LINK)
					onLinkShareCallBack.onLinkShareCallBack(true);
				else
					onPhotoShareCallBack.onPhotoShareCallBack(true);
			} else {
				Log.d(TAG, "Google::onActivityResult 分享成功");
				if (shareType == ShareType.LINK)
					onLinkShareCallBack.onLinkShareCallBack(false);
				else
					onPhotoShareCallBack.onPhotoShareCallBack(false);
			}

		} else if (requestCode == REQUEST_CODE_PAY) {
			helper.handleActivityResult(requestCode, resultCode, data);
		}
	}

	// 查询购买后没有消费的商品
	public void queryInventoryAsync(final List<String> skuList,
			final OnQueryInventoryCallBack onQueryInventoryCallBack) {
		Log.d(TAG, "Google::queryInventoryAsync 查询用户具有的商品信息");
		try {

			helper.queryInventoryAsync(new IabHelper.QueryInventoryFinishedListener() {
				public void onQueryInventoryFinished(IabResult result,
						Inventory inventory) {

					ArrayList<GoogleSkuModel> trans = new ArrayList<GoogleSkuModel>();

					if (result.isFailure()) {
						// Handle failure
						System.out
								.println(TAG
										+ " Google::queryInventoryAsync, 查询在谷歌购买的商品失败, 原因:"
										+ result.getMessage());
						onQueryInventoryCallBack.onQueryInventoryCallBack(
								false, trans);
					} else {
						System.out.println(TAG
								+ " Google::queryInventoryAsync, 查询在谷歌购买的商品成功");
						// 漏单处理
						for (String sku : skuList) {

							if (inventory.hasDetails(sku)) {
								SkuDetails detail = inventory
										.getSkuDetails(sku);
								GoogleSkuModel googleSkuModel = new GoogleSkuModel();
								Log.d(TAG, detail.toString());
								googleSkuModel.description = detail
										.getDescription();
								googleSkuModel.price = detail.getPrice();
								googleSkuModel.sku = detail.getSku();
								googleSkuModel.title = detail.getTitle();
								googleSkuModel.type = detail.getType();
								googleSkuModel.priceAmountMicros = (new BigDecimal(
										detail.getPriceAmountMicros())
										.divide(new BigDecimal(1000000)))
										.toString();
								googleSkuModel.priceCurrencyCode = detail
										.getPriceCurrencyCode();
								// 存储所有未处理订单
								if (inventory.hasPurchase(sku)) {
									Purchase purchase = inventory
											.getPurchase(sku);
									System.out
											.println(TAG
													+ " Google::queryInventoryAsync, 验证的信息："
													+ purchase
															.getOriginalJson());
									googleSkuModel.googleOrder = purchase
											.getOrderId();
									googleSkuModel.signature = purchase
											.getSignature();
									googleSkuModel.purchaseOriginalData = purchase
											.getOriginalJson();
									googleSkuModel.itemType = purchase
											.getItemType();
									System.out
											.println(TAG
													+ " Google::queryInventoryAsync, 商品信息："
													+ detail.toString());
								}

								trans.add(googleSkuModel);
							}

						}

						onQueryInventoryCallBack.onQueryInventoryCallBack(true,
								trans);

					}
				}
			}

			);
		} catch (Exception e) {
			Log.d(TAG, "查询用户具有的商品信息失败，异常信息：" + e.getMessage());
			Log.d(TAG, "请检查如下情况：");
			Log.d(TAG, "1. 请检查手机上是否有谷歌框架（可以查看是否有谷歌商店）");
			Log.d(TAG, "2. 请检查手机上是否有谷歌账号（可以进入谷歌商店看是否有账号）");
			Log.d(TAG, "3. 请检查谷歌账号是否能够支付（可以进入谷歌商店看是否有能花钱购买的app）");
			Log.d(TAG, "4. 请检查谷歌账号是否是测试账号（去谷歌后台查看）");
			Log.d(TAG, "5. 请检查谷歌账号是接收了测试邀请（去谷歌后台获取测试邀请链接打开）");
			Log.d(TAG, "6. 请检查APK签名是否与提交给谷歌的APK相同");
			Log.d(TAG, "7. 请检查APK VersionCode是否与提交给谷歌的APK相同");
			Log.d(TAG, "8. 请检查是否在谷歌后台正确配置了商品");
			e.printStackTrace();
		}
	}

	// 查询商品信息
	public void querySkus(ArrayList<String> skus,
			final OnQuerySkusDetailsCallBack onQuerySkusDetailsCallBack) {
		try {
			helper.querySkuDetailsAsync(skus, IabHelper.ITEM_TYPE_INAPP,
					new IabHelper.QuerySkuDetailsFinishedListener() {

						@Override
						public void onQuerySkuDetailsFinished(IabResult result,
								List<SkuDetails> skuDetails) {

							if (result.isFailure()) {
								if (onQuerySkusDetailsCallBack != null)
									onQuerySkusDetailsCallBack
											.onQuerySkusDetailsCallBack(
													false,
													new ArrayList<GoogleSkuModel>());
							} else {

								ArrayList<GoogleSkuModel> objgsm = new ArrayList<GoogleSkuModel>();

								for (SkuDetails skudetail : skuDetails) {
									BigDecimal mFloat = new BigDecimal(
											skudetail.getPriceAmountMicros())
											.divide(new BigDecimal(1000000));
									System.out.println(TAG
											+ "Google::querySkus, 获得谷歌后台配置的商品:"
											+ skudetail.toString());

									GoogleSkuModel inf = new GoogleSkuModel();
									inf.description = skudetail
											.getDescription();
									inf.price = skudetail.getPrice();
									inf.sku = skudetail.getSku();
									inf.title = skudetail.getTitle();
									inf.type = skudetail.getType();
									inf.priceAmountMicros = mFloat.toString();
									inf.priceCurrencyCode = skudetail
											.getPriceCurrencyCode();

									objgsm.add(inf);
								}

								onQuerySkusDetailsCallBack
										.onQuerySkusDetailsCallBack(true,
												objgsm);

							}
						}
					});
		} catch (Exception e) {
			e.printStackTrace();
			Log.d(TAG, "获取谷歌后台配置的商品列表失败");
			Log.d(TAG, "请检查如下情况：");
			Log.d(TAG, "1. 请检查手机上是否有谷歌框架（可以查看是否有谷歌商店）");
			Log.d(TAG, "2. 请检查手机上是否有谷歌账号（可以进入谷歌商店看是否有账号）");
			Log.d(TAG, "3. 请检查谷歌账号是否能够支付（可以进入谷歌商店看是否有能花钱购买的app）");
			Log.d(TAG, "4. 请检查谷歌账号是否是测试账号（去谷歌后台查看）");
			Log.d(TAG, "5. 请检查谷歌账号是接收了测试邀请（去谷歌后台获取测试邀请链接打开）");
			Log.d(TAG, "6. 请检查APK签名是否与提交给谷歌的APK相同");
			Log.d(TAG, "7. 请检查APK VersionCode是否与提交给谷歌的APK相同");
			Log.d(TAG, "8. 请检查是否在谷歌后台正确配置了商品");
		}
	}

	// 支付 sku商品ID
	public void pay(final String sku, final OnPayCallBack onPayCallBack) {
		try {
			// 为了防止谷歌购买成功，但是没有收到正确结果情况出现，需要把当前数据存储
			Log.d(TAG, "Google::pay");
			helper.launchPurchaseFlow(activity, sku, REQUEST_CODE_PAY,
					new IabHelper.OnIabPurchaseFinishedListener() {
						public void onIabPurchaseFinished(IabResult result,
								Purchase purchase) {

							if (result.isFailure()) {
								// Handle error
								Log.d(TAG, result.getMessage());
								onPayCallBack.onPayCallBack(false, sku, "", "",
										"", "");

							} else if ((purchase.getSku().equals(sku))) {

								onPayCallBack.onPayCallBack(true, sku,
										purchase.getItemType(),
										purchase.getOrderId(),
										purchase.getSignature(),
										purchase.getOriginalJson());
							}
						}
					});
		} catch (Exception e) {
			e.printStackTrace();
			Log.d(TAG, "启动谷歌支付失败");
			Log.d(TAG, "请检查如下情况：");
			Log.d(TAG, "1. 请检查手机上是否有谷歌框架（可以查看是否有谷歌商店）");
			Log.d(TAG, "2. 请检查手机上是否有谷歌账号（可以进入谷歌商店看是否有账号）");
			Log.d(TAG, "3. 请检查谷歌账号是否能够支付（可以进入谷歌商店看是否有能花钱购买的app）");
			Log.d(TAG, "4. 请检查谷歌账号是否是测试账号（去谷歌后台查看）");
			Log.d(TAG, "5. 请检查谷歌账号是接收了测试邀请（去谷歌后台获取测试邀请链接打开）");
			Log.d(TAG, "6. 请检查APK签名是否与提交给谷歌的APK相同");
			Log.d(TAG, "7. 请检查APK VersionCode是否与提交给谷歌的APK相同");
			Log.d(TAG, "8. 请检查是否在谷歌后台正确配置了商品");
		}

	}

	// 支付订阅 sku商品ID
	public void paysubscribe(final String sku, final OnPayCallBack onPayCallBack) {
		try {
			// 为了防止谷歌购买成功，但是没有收到正确结果情况出现，需要把当前数据存储
			Log.d(TAG, "Google::pay");
			helper.launchSubscriptionPurchaseFlow(activity, sku, REQUEST_CODE_PAY,
					new IabHelper.OnIabPurchaseFinishedListener() {
						public void onIabPurchaseFinished(IabResult result,
								Purchase purchase) {

							if (result.isFailure()) {
								// Handle error
								Log.d(TAG, result.getMessage());
								onPayCallBack.onPayCallBack(false, sku, "", "",
										"", "");

							} else if ((purchase.getSku().equals(sku))) {

								onPayCallBack.onPayCallBack(true, sku,
										purchase.getItemType(),
										purchase.getOrderId(),
										purchase.getSignature(),
										purchase.getOriginalJson());
							}
						}
					});
		} catch (Exception e) {
			e.printStackTrace();
			Log.d(TAG, "启动谷歌支付失败");
			Log.d(TAG, "请检查如下情况：");
			Log.d(TAG, "1. 请检查手机上是否有谷歌框架（可以查看是否有谷歌商店）");
			Log.d(TAG, "2. 请检查手机上是否有谷歌账号（可以进入谷歌商店看是否有账号）");
			Log.d(TAG, "3. 请检查谷歌账号是否能够支付（可以进入谷歌商店看是否有能花钱购买的app）");
			Log.d(TAG, "4. 请检查谷歌账号是否是测试账号（去谷歌后台查看）");
			Log.d(TAG, "5. 请检查谷歌账号是接收了测试邀请（去谷歌后台获取测试邀请链接打开）");
			Log.d(TAG, "6. 请检查APK签名是否与提交给谷歌的APK相同");
			Log.d(TAG, "7. 请检查APK VersionCode是否与提交给谷歌的APK相同");
			Log.d(TAG, "8. 请检查是否在谷歌后台正确配置了商品");
		}

	}
	
	
	public void consume(final String sku, String itemType, String signature,
			String purchaseOriginalData,
			final OnConsumeCallBack onConsumeCallBack) {
		Purchase purchase;
		try {
			purchase = new Purchase(itemType, purchaseOriginalData, signature);

			helper.consumeAsync(purchase,
					new IabHelper.OnConsumeFinishedListener() {
						public void onConsumeFinished(Purchase purchase,
								IabResult result) {
							if (result.isSuccess()) {
								Log.d(TAG, " 商品在谷歌消费成功");
								// 消费成功，需要去平台进行验证
								onConsumeCallBack.onConsumeCallBack(true, sku);
							} else {
								Log.d(TAG,
										" 商品在谷歌消费失败，原因：" + result.getMessage());
								onConsumeCallBack.onConsumeCallBack(false, sku);

							}
						}
					});
		} catch (JSONException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
	}

	public void onDestroy() {
		if (helper != null) {
			helper.dispose();
			helper = null;
		}
	}

	private boolean checkPlayServices(Activity activity) {
		int resultCode = GooglePlayServicesUtil
				.isGooglePlayServicesAvailable(activity);
		if (resultCode != ConnectionResult.SUCCESS) {
			if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
				GooglePlayServicesUtil.getErrorDialog(resultCode, activity,
						PLAY_SERVICES_RESOLUTION_REQUEST).show();
			} else {

			}
			return false;
		}
		return true;
	}

}

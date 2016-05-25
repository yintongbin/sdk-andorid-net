package com.seastargames.android.sdk;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphRequest.GraphJSONArrayCallback;
import com.facebook.GraphResponse;
import com.facebook.GraphRequest.Callback;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.share.ShareApi;
import com.facebook.share.Sharer;
import com.facebook.share.Sharer.Result;
import com.facebook.share.internal.ShareFeedContent;
import com.facebook.share.model.AppInviteContent;
import com.facebook.share.model.GameRequestContent;
import com.facebook.share.model.GameRequestContent.ActionType;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.model.ShareOpenGraphAction;
import com.facebook.share.model.ShareOpenGraphContent;
import com.facebook.share.model.ShareOpenGraphObject;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.AppInviteDialog;
import com.facebook.share.widget.GameRequestDialog;
import com.facebook.share.widget.ShareDialog;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

// 所有功能必须都在登录条件下起作用
public class Facebook {

	public static final String TAG = "SeaStar";

	public interface OnLoginCallBack {

		void onLoginCallBack(boolean isLogin, String user);
	}

	public interface OnInitCallBack {
		void onInitCallBack(boolean isInit);
	}

	public interface OnPhotoShareCallBack {
		void onPhotoShareCallBack(boolean success);
	}

	public interface OnUrlShareCallBack {
		void onUrlShareCallBack(boolean success);
	}

	public interface OnGameShareCallBack {
		void onGameShareCallBack(boolean success);
	}

	public interface OnShareGameToFacebookHomepageCallBack {
		void onShareGameToFacebookHomepageCallBack(boolean success);
	}

	public interface OnShareFeedCallBack {
		void onShareFeedCallBack(boolean success);
	}

	public interface OnInviteCallBack {
		void onInviteCallBack(boolean success);
	}

	public interface OnInviteFriendsInfoCallBack {
		void onInviteFriendsInfoCallBack(String graph);
	}

	public interface OnAllFriendsCallBack {
		void onAllFriendsCallBack(String graph);
	}

	public interface OnFriendsInAppCallBack {
		void onFriendsInAppCallBack(String graph);
	}

	private OnLoginCallBack onLoginCallBack;

	private Activity activity = null;
	private CallbackManager callbackManager;

	public String nextPos;
	public String prevPos;

	public void init(final OnInitCallBack oick) {

		FacebookSdk.sdkInitialize(activity.getApplicationContext(),
				new FacebookSdk.InitializeCallback() {
					@Override
					public void onInitialized() {
						if (FacebookSdk.isInitialized()) {
							Log.d(TAG, "Facebook::init, init success!");
							oick.onInitCallBack(true);
						} else {
							Log.d(TAG, "Facebook::init, init failed!");
							oick.onInitCallBack(false);
						}
					}
				});

		callbackManager = CallbackManager.Factory.create();
		LoginManager.getInstance().registerCallback(callbackManager,
				new FacebookCallback<LoginResult>() {
					@Override
					public void onSuccess(LoginResult loginResult) {
						Log.d(TAG, "Facebook::login login success!");
						getMyInfo();
					}

					@Override
					public void onCancel() {
						Log.d(TAG, "Facebook::login login cancel!");
						onLoginCallBack.onLoginCallBack(false, null);
						onLoginCallBack = null;
					}

					@Override
					public void onError(FacebookException e) {
						Log.d(TAG, "请检查如下情况：");
						Log.d(TAG, "1: APK的keyhash是否跟facebook后台一致");
						Log.d(TAG, "2: 包名是否跟facebook后台一致");
						Log.d(TAG, "3: facebook后台的display name是否设置了");
						Log.d(TAG, "4: facebook后台的启动Activity全路径是否设置了");
						Log.d(TAG, "5: 如果是测试环境，是否使用的facebook测试账号");
						Log.d(TAG,
								"Facebook::login login error! : "
										+ e.toString());
						onLoginCallBack.onLoginCallBack(false, null);
						onLoginCallBack = null;
					}
				});
	}

	// 申请访问账号信息、好友信息、发布分享的权限
	public void login(OnLoginCallBack onLoginCallBack) {
		this.onLoginCallBack = onLoginCallBack;
		AccessToken token = AccessToken.getCurrentAccessToken();
		if (token != null && !token.isExpired()) {
			getMyInfo();
			return;
		}

		LoginManager.getInstance().logInWithReadPermissions(
				(Activity) activity,
				Arrays.asList("public_profile", "user_friends"));
	}

	public void logout() {
		LoginManager.getInstance().logOut();
	}

	// imageUri 图片uri （照片必须小于12MB大小并且需要安装版本7或更高版本的应用程序）
	// caption 图片标题
	// peopleIds 分享到的好友id
	// contentUri 内容uri
	// refUrl 推荐网址
	public void sharePhotoByUri(String imageUri, String caption,
			List<String> peopleIds, String contentUri, String refUrl,
			final OnPhotoShareCallBack listener) {

		List<SharePhoto> photos = new ArrayList<SharePhoto>();
		SharePhoto photo = new SharePhoto.Builder()
				.setImageUrl(Uri.parse(imageUri)).setCaption(caption).build();
		photos.add(photo);

		SharePhotoContent.Builder builder = new SharePhotoContent.Builder();
		builder.addPhotos(photos);
		if (contentUri != null)
			builder.setContentUrl(Uri.parse(contentUri));
		if (peopleIds != null)
			builder.setPeopleIds(peopleIds);
		if (refUrl != null)
			builder.setRef(refUrl);
		SharePhotoContent sharePhotoContent = builder.build();

		FacebookCallback<Sharer.Result> shareCallback = new FacebookCallback<Sharer.Result>() {

			@Override
			public void onSuccess(Result result) {
				Log.d(TAG, "Facebook::sharePhotoByUri facebook分享成功, 结果:"
						+ result);
				listener.onPhotoShareCallBack(true);
			}

			@Override
			public void onCancel() {
				Log.d(TAG, "Facebook::sharePhotoByUri facebook取消分享");
				listener.onPhotoShareCallBack(false);
			}

			@Override
			public void onError(FacebookException error) {
				// TODO Auto-generated method stub
				Log.d(TAG, "Facebook::sharePhotoByUri facebook分享失败，失败信息:"
						+ error);
				listener.onPhotoShareCallBack(false);
			}
		};

		// 进行分享
		if (ShareDialog.canShow(SharePhotoContent.class)) {
			// 使用apps分享
			ShareDialog shareDialog = new ShareDialog(activity);
			shareDialog.registerCallback(callbackManager, shareCallback);
			shareDialog.show(sharePhotoContent);
		} else {
			// dialog不能分享，使用graph分享
			ShareApi.share(sharePhotoContent, shareCallback);
		}
	}

	// bitmap 图片 （照片必须小于12MB大小并且需要安装版本7或更高版本的应用程序）
	// caption 图片标题
	// peopleIds 分享到的好友id
	// contentUri 内容uri
	// refUrl 推荐网址
	public void sharePhoto(Bitmap bitmap, String caption,
			List<String> peopleIds, Uri contentUri, String refUrl,
			final OnPhotoShareCallBack listener) {

		List<SharePhoto> photos = new ArrayList<SharePhoto>();
		SharePhoto photo = new SharePhoto.Builder().setBitmap(bitmap)
				.setCaption(caption).build();
		photos.add(photo);

		SharePhotoContent.Builder builder = new SharePhotoContent.Builder();
		builder.addPhotos(photos);
		if (contentUri != null)
			builder.setContentUrl(contentUri);
		if (peopleIds != null)
			builder.setPeopleIds(peopleIds);
		if (refUrl != null)
			builder.setRef(refUrl);
		if (peopleIds != null)
			builder.setPeopleIds(peopleIds);
		SharePhotoContent sharePhotoContent = builder.build();

		FacebookCallback<Sharer.Result> shareCallback = new FacebookCallback<Sharer.Result>() {

			@Override
			public void onSuccess(Result result) {
				Log.d(TAG, "Facebook::sharePhoto facebook分享成功, 结果:" + result);
				listener.onPhotoShareCallBack(true);
			}

			@Override
			public void onCancel() {
				Log.d(TAG, "Facebook::sharePhoto facebook取消分享");
				listener.onPhotoShareCallBack(false);
			}

			@Override
			public void onError(FacebookException error) {
				// TODO Auto-generated method stub
				Log.d(TAG, "Facebook::sharePhoto facebook分享失败，失败信息:" + error);
				listener.onPhotoShareCallBack(false);
			}
		};

		// 进行分享
		if (ShareDialog.canShow(SharePhotoContent.class)) {
			// 使用apps分享
			ShareDialog shareDialog = new ShareDialog(activity);
			shareDialog.registerCallback(callbackManager, shareCallback);
			shareDialog.show(sharePhotoContent);
		} else {
			// dialog不能分享，使用graph分享
			ShareApi.share(sharePhotoContent, shareCallback);
		}
	}

	// url 链接
	// title 链接内容的标题
	// contentDescription 描述内容
	// imageUrl 缩略图图像的URL
	public void shareUrl(String url, String title, String contentDescription,
			String imageUrl, List<String> peopleIds,
			final OnUrlShareCallBack listener) {

		ShareLinkContent.Builder builder = new ShareLinkContent.Builder();
		builder.setContentUrl(Uri.parse(url));
		builder.setContentTitle(title);
		builder.setImageUrl(Uri.parse(imageUrl));
		builder.setContentDescription(contentDescription);
		if (peopleIds != null) {
			builder.setPeopleIds(peopleIds);
		}

		ShareLinkContent content = builder.build();

		FacebookCallback<Sharer.Result> shareCallback = new FacebookCallback<Sharer.Result>() {

			@Override
			public void onSuccess(Result result) {
				Log.d(TAG, "Facebook::shareUrl facebook分享成功, 结果:" + result);
				listener.onUrlShareCallBack(true);
			}

			@Override
			public void onCancel() {
				Log.d(TAG, "Facebook::shareUrl facebook取消分享");
				listener.onUrlShareCallBack(false);
			}

			@Override
			public void onError(FacebookException error) {
				// TODO Auto-generated method stub
				Log.d(TAG, "Facebook::shareUrl facebook分享失败，失败信息:" + error);
				listener.onUrlShareCallBack(false);
			}
		};

		if (ShareDialog.canShow(ShareLinkContent.class)) {
			ShareDialog shareDialog = new ShareDialog(activity);
			shareDialog.registerCallback(callbackManager, shareCallback);
			shareDialog.show(content);
		} else {
			ShareApi.share(content, shareCallback);
		}
	}

	// 向指定好友分享游戏
	public void shareGame(String title, String message, String urerId,
			String ObjectId, final OnGameShareCallBack listener) {

		GameRequestDialog shareGameDialog = new GameRequestDialog(activity);
		GameRequestContent content = null;
		if (urerId != null && ObjectId == null) {
			content = new GameRequestContent.Builder().setMessage(message)
					.setTitle(title).setTo(urerId).build();
		} else if (urerId == null && ObjectId != null) {
			content = new GameRequestContent.Builder().setMessage(message)
					.setActionType(ActionType.SEND).setObjectId(ObjectId)
					.setTitle(title).build();
		} else {
			content = new GameRequestContent.Builder().setMessage(message)
					.setTitle(title).build();
		}
		shareGameDialog.registerCallback(callbackManager,
				new FacebookCallback<GameRequestDialog.Result>() {

					@Override
					public void onSuccess(
							com.facebook.share.widget.GameRequestDialog.Result result) {
						Log.d(TAG, "Facebook::shareGame facebook分享成功, 结果:"
								+ result);
						listener.onGameShareCallBack(true);
					}

					@Override
					public void onError(FacebookException error) {
						// TODO Auto-generated method stub
						Log.d(TAG, "Facebook::shareGame facebook分享失败，失败信息:"
								+ error);
						listener.onGameShareCallBack(false);
					}

					@Override
					public void onCancel() {
						// TODO Auto-generated method stub
						Log.d(TAG, "Facebook::shareGame facebook取消分享");
						listener.onGameShareCallBack(false);
					}
				});
		shareGameDialog.show(content);
	}

	// 分享到facebook主页（类似朋友圈）
	public void shareGameToFacebookHomepage(String actionType,
			String objectKey, String previewPropertyName,
			ArrayList<String> list,
			final OnShareGameToFacebookHomepageCallBack listener) {

		ShareDialog shareDialog = new ShareDialog(activity);
		shareDialog.registerCallback(callbackManager,
				new FacebookCallback<Sharer.Result>() {

					@Override
					public void onSuccess(Result result) {
						Log.d(TAG,
								"Facebook::shareGameToFacebookHomepage facebook分享成功, 结果:"
										+ result);
						listener.onShareGameToFacebookHomepageCallBack(true);
					}

					@Override
					public void onCancel() {
						Log.d(TAG,
								"Facebook::shareGameToFacebookHomepage facebook取消分享");
						listener.onShareGameToFacebookHomepageCallBack(false);
					}

					@Override
					public void onError(FacebookException error) {
						// TODO Auto-generated method stub
						Log.d(TAG,
								"Facebook::shareGameToFacebookHomepage facebook分享失败，失败信息:"
										+ error);
						listener.onShareGameToFacebookHomepageCallBack(false);
					}
				});

		// Create an object
		ShareOpenGraphObject object = new ShareOpenGraphObject.Builder()
				.putString("og:type", "books.book")
				.putString("og:title", "A Game of Thrones")
				.putString(
						"og:description",
						"In the frozen wastes to the north of Winterfell, sinister and supernatural forces are mustering.")
				.putString("books:isbn", "0-553-57340-3").build();
		// Create an action
		ShareOpenGraphAction action = new ShareOpenGraphAction.Builder()
				.setActionType(actionType).putObject(objectKey, object).build();
		// Create the content
		ShareOpenGraphContent content = new ShareOpenGraphContent.Builder()
				.setPreviewPropertyName(previewPropertyName).setAction(action)
				.build();
		shareDialog.show(content);
	}

	// applink 游戏链接
	// linkName 链接名称
	// caption 标题
	// description 内容
	// imageUrl 图片链接
	// peopleIds
	// toId
	// 另一种链接分享方式，使用web页面分享
	public void shareFeed(String applink, String linkName, String caption,
			String description, String imageUrl, List<String> peopleIds,
			String toId, final OnShareFeedCallBack listener) {

		ShareFeedContent.Builder builder = new ShareFeedContent.Builder();
		builder.setToId(toId);
		builder.setLink(applink);
		builder.setLinkName(linkName);
		builder.setLinkCaption(caption);
		builder.setLinkDescription(description);
		builder.setPicture(imageUrl);
		if (peopleIds != null)
			builder.setPeopleIds(peopleIds);
		if (toId != null)
			builder.setToId(toId);
		ShareFeedContent shareContent = builder.build();

		ShareDialog shareDialog = new ShareDialog(activity);
		shareDialog.registerCallback(callbackManager,
				new FacebookCallback<Sharer.Result>() {

					@Override
					public void onSuccess(Result result) {
						Log.d(TAG,
								"Facebook::onShareFeedCallBack facebook分享成功, 结果:"
										+ result);
						listener.onShareFeedCallBack(true);
					}

					@Override
					public void onCancel() {
						Log.d(TAG, "Facebook::onShareFeedCallBack facebook取消分享");
						listener.onShareFeedCallBack(false);
					}

					@Override
					public void onError(FacebookException error) {
						// TODO Auto-generated method stub
						Log.d(TAG,
								"Facebook::onShareFeedCallBack facebook分享失败，失败信息:"
										+ error);
						listener.onShareFeedCallBack(false);
					}
				});
		shareDialog.show(shareContent);
	}

	// 邀请好友
	public void inviteFriends(String applinkUrl, String previewImageUrl,
			final OnInviteCallBack listener) {
		AppInviteContent appInviteContent = new AppInviteContent.Builder()
				.setApplinkUrl(applinkUrl).setPreviewImageUrl(previewImageUrl)
				.build();

		AppInviteDialog appInviteDialog = new AppInviteDialog(activity);
		appInviteDialog.show(appInviteContent);
		appInviteDialog.registerCallback(callbackManager,
				new FacebookCallback<AppInviteDialog.Result>() {
					@Override
					public void onSuccess(
							com.facebook.share.widget.AppInviteDialog.Result result) {
						// TODO Auto-generated method stub
						Log.d(TAG,
								"Facebook::inviteFriends facebook邀请好友成功, 结果："
										+ result.toString());
						listener.onInviteCallBack(true);
					}

					@Override
					public void onError(FacebookException error) {
						// TODO Auto-generated method stub
						Log.d(TAG,
								"Facebook::inviteFriends facebook邀请好友失败, 错误信息："
										+ error.getMessage());
						listener.onInviteCallBack(false);
					}

					@Override
					public void onCancel() {
						// TODO Auto-generated method stub
						Log.d(TAG, "Facebook::inviteFriends facebook取消好友邀请");
						listener.onInviteCallBack(false);
					}
				});
	}

	// 获取邀请的好友信息
	public void requestInviteFriendsInfo(String limit,
			final OnInviteFriendsInfoCallBack listener) {
		GraphRequest request = GraphRequest.newGraphPathRequest(
				AccessToken.getCurrentAccessToken(), "me/invitable_friends",
				new Callback() {
					@Override
					public void onCompleted(GraphResponse response) {
						Log.d(TAG,
								"Facebook::requestInvitFriends inviteFriends------response:"
										+ response.toString());
						listener.onInviteFriendsInfoCallBack(response
								.toString());
					}
				});

		Bundle parameter = new Bundle();
		if (limit != null) {
			parameter.putString("limit", limit);
		}
		request.setParameters(parameter);
		request.executeAsync();
	}

	public void requestAllFriends(String height, String width, String limit,
			final OnAllFriendsCallBack listener) {
		GraphRequest graphRequest = GraphRequest.newGraphPathRequest(
				AccessToken.getCurrentAccessToken(), "me/taggable_friends",
				new Callback() {
					@Override
					public void onCompleted(GraphResponse response) {
						Log.d(TAG,
								"Facebook::requestAllFriends facebook获取用户好友的信息------response:"
										+ response.toString());

						parseAllFriendsInfo(response.getJSONObject().toString());
						listener.onAllFriendsCallBack(response.getJSONObject()
								.toString());
					}
				});
		String field[] = { "id", "name",
				"picture.height(" + height + ").width(" + width + ")" };
		Bundle parameter = graphRequest.getParameters();
		if (limit != null) {
			parameter.putString("limit", limit);
		}
		parameter.putString("fields", TextUtils.join(",", field));
		graphRequest.setParameters(parameter);
		Log.d(TAG, graphRequest.toString());
		GraphRequest.executeBatchAsync(graphRequest);
	}

	// 获取下一步分页信息

	public void requestAllFriendsNextPage(final OnAllFriendsCallBack listener) {
		if (nextPos == null)
			return;

		OkHttpClient client = new OkHttpClient();
		Request request = new Request.Builder().url(nextPos).build();
		client.newCall(request).enqueue(new okhttp3.Callback() {

			@Override
			public void onResponse(Call arg0, Response response)
					throws IOException {
				// TODO 自动生成的方法存根
				String responseCall = response.body().string();
				Log.d(TAG, responseCall);
				parseAllFriendsInfo(responseCall);
				listener.onAllFriendsCallBack(responseCall);

			}

			@Override
			public void onFailure(Call arg0, IOException arg1) {
				// TODO 自动生成的方法存根

			}
		});

	}

	// 获取上一步分页信息
	public void requestAllFriendsPrevPage(final OnAllFriendsCallBack listener) {
		if (prevPos == null)
			return;

		OkHttpClient client = new OkHttpClient();
		Request request = new Request.Builder().url(prevPos).build();
		client.newCall(request).enqueue(new okhttp3.Callback() {

			@Override
			public void onResponse(Call arg0, Response response)
					throws IOException {
				// TODO 自动生成的方法存根
				String responseCall = response.body().string();
				Log.d(TAG, responseCall);
				parseAllFriendsInfo(responseCall);
				listener.onAllFriendsCallBack(responseCall);

			}

			@Override
			public void onFailure(Call arg0, IOException arg1) {
				// TODO 自动生成的方法存根

			}
		});
	}

	// 获取本应用中FB的好友信息
	public void requestFriendsInApp(String limit,
			final OnFriendsInAppCallBack listener) {

		/*-----------------------------------获取用户的信息------  -------------------------------------*/
		GraphRequest request = GraphRequest.newMyFriendsRequest(
				AccessToken.getCurrentAccessToken(),
				new GraphJSONArrayCallback() {

					@Override
					public void onCompleted(JSONArray objects,
							GraphResponse response) {
						Log.d(TAG,
								"Facebook::requestFriendsInApp facebook 获取好友的信息 :"
										+ response.toString());

						if (response.getJSONObject() != null) {
							parseAllFriendsInfo(response.getJSONObject()
									.toString());
							listener.onFriendsInAppCallBack(response
									.getJSONObject().toString());
						} else {
							listener.onFriendsInAppCallBack("");
						}
					}
				});

		Bundle parameters = new Bundle();
		parameters
				.putString(
						"fields",
						"id,name,picture,email,first_name,last_name,middle_name,name_format,third_party_id,gender");
		if (limit != null) {
			parameters.putString("limit", limit);
		}
		request.setParameters(parameters);
		request.executeAsync();
	}

	// 获取下一步分页信息
	public void requestFriendsInAppNextPage(
			final OnFriendsInAppCallBack listener) {
		if (nextPos == null)
			return;

		OkHttpClient client = new OkHttpClient();
		Request request = new Request.Builder().url(nextPos).build();
		client.newCall(request).enqueue(new okhttp3.Callback() {

			@Override
			public void onResponse(Call arg0, Response response)
					throws IOException {
				// TODO 自动生成的方法存根
				String responseCall = response.body().string();
				parseAllFriendsInfo(responseCall);
				listener.onFriendsInAppCallBack(responseCall);
				Log.d(TAG, responseCall);
			}

			@Override
			public void onFailure(Call arg0, IOException arg1) {
				// TODO 自动生成的方法存根

			}
		});

	}

	// 获取上一步分页信息
	public void requestFriendsInAppPrevPage(
			final OnFriendsInAppCallBack listener) {
		if (prevPos == null)
			return;

		OkHttpClient client = new OkHttpClient();
		Request request = new Request.Builder().url(prevPos).build();
		client.newCall(request).enqueue(new okhttp3.Callback() {

			@Override
			public void onResponse(Call arg0, Response response)
					throws IOException {
				// TODO 自动生成的方法存根
				String responseCall = response.body().string();
				parseAllFriendsInfo(responseCall);
				listener.onFriendsInAppCallBack(responseCall);
				Log.d(TAG, responseCall);
			}

			@Override
			public void onFailure(Call arg0, IOException arg1) {
				// TODO 自动生成的方法存根

			}
		});

	}

	public void onActivityResult(final int requestCode, final int resultCode,
			final Intent data) {
		callbackManager.onActivityResult(requestCode, resultCode, data);
		AppEventsLogger.activateApp(activity);
	}

	public void setCurActivity(Activity activity) {
		this.activity = activity;
	}

	private void parseAllFriendsInfo(String response) {
		try {
			JSONObject obj = null;
			obj = new JSONObject(response);
			JSONArray data = null;
			if (obj.has("data")) {
				data = obj.getJSONArray("data");
			}
			String paging = "";
			if (obj.has("paging")) {
				paging = obj.getString("paging");
			}
			JSONObject pagingObj = null;
			if (paging != "") {
				pagingObj = new JSONObject(paging.toString());
			}
			if (data != null && data.length() > 0) {
				if (pagingObj.has("next")) {
					nextPos = pagingObj.getString("next");
				} else {
					nextPos = null;
				}
				if (pagingObj.has("previous")) {
					prevPos = pagingObj.getString("previous");
				} else {
					prevPos = null;
				}
			} else {
				nextPos = null;
				prevPos = null;
			}
			Log.d(TAG, "nextStr------response:" + nextPos);
			Log.d(TAG, "previous------response:" + prevPos);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 获取用户自己的信息
	 */
	private void getMyInfo() {
		if (AccessToken.getCurrentAccessToken() == null) {
			Log.d(TAG, "Facebook::getMyInfo facebook获取自己的信息失败，facebook没有登录");
			onLoginCallBack.onLoginCallBack(false, null);
			onLoginCallBack = null;
			return;
		}
		/*-----------------------------------获取用户的信息-------------------------------------------*/
		GraphRequest request = GraphRequest.newMeRequest(
				AccessToken.getCurrentAccessToken(),
				new GraphRequest.GraphJSONObjectCallback() {

					@Override
					public void onCompleted(JSONObject object,
							GraphResponse response) {
						// Application code
						Log.d(TAG, "Facebook::getMyInfo facebook获取用户的信息 :"
								+ object.toString());
						if (response.getJSONObject() != null) {
							JSONObject json = response.getJSONObject();
							onLoginCallBack.onLoginCallBack(true,
									json.toString());
							onLoginCallBack = null;

						} else {
							onLoginCallBack.onLoginCallBack(false, null);
							onLoginCallBack = null;
						}
					}
				});
		Bundle parameters = new Bundle();
		parameters
				.putString(
						"fields",
						"id,name,picture,email,first_name,last_name,middle_name,name_format,third_party_id,gender,location,friends");
		request.setParameters(parameters);
		request.executeAsync();

	}

	public void onResume() {
		AppEventsLogger.activateApp(activity);
	}

}

package com.example.admin.hn.ui.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.admin.hn.R;
import com.example.admin.hn.api.Api;
import com.example.admin.hn.base.BaseActivity;
import com.example.admin.hn.http.OkHttpUtil;
import com.example.admin.hn.model.CompanyInfo;
import com.example.admin.hn.model.ServerResponse;
import com.example.admin.hn.utils.GsonUtils;
import com.example.admin.hn.utils.ToolAlert;
import com.example.admin.hn.widget.TimeButton;
import com.orhanobut.logger.Logger;
import com.squareup.okhttp.Request;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author duantao
 * @date on 2017/8/3 14:45
 * @describe 注册
 */
public class RegisterActivity extends BaseActivity {

	private static final String TAG = "RegisterActivity";
	@Bind(R.id.tb_send_validate_code)
	TimeButton mTbSendValidateCode;
	@Bind(R.id.text_title_back)
	TextView mTextTitleBack;
	@Bind(R.id.text_title)
	TextView mTextTitle;
	@Bind(R.id.et_username)
	EditText mEditUsername;
	@Bind(R.id.et_telephone)
	EditText mEditTelephone;
	@Bind(R.id.et_validate_code)
	EditText mEditCode;
	@Bind(R.id.et_login_password)
	EditText mEditPassword;
	@Bind(R.id.et_confirm_password)
	EditText mEditRepeatpassword;
	@Bind(R.id.cb_agree_protocol)
	CheckBox mCbAgreeProtocol;
	@Bind(R.id.et_email)
	EditText mEditemail;
	@Bind(R.id.et_company)
	EditText et_company;
	@Bind(R.id.sp_environment)
	Spinner environment;
	@Bind(R.id.cb)
	CheckBox cb;
	private String url_register = Api.BASE_URL + Api.REGISTER;
	private String url_email = Api.BASE_URL + Api.EMAIL;
	private String url_companyname = Api.BASE_URL + Api.COMPANYNAME;
	private List<String> data_list;
	private ArrayAdapter<String> arr_adapter;
	private String company;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		ButterKnife.bind(this);
		mTbSendValidateCode.onCreate(savedInstanceState);
		initTitleBar();
		initView();
	}


	public static void startActivity(Context context){
		Intent intent = new Intent(context, RegisterActivity.class);
		context.startActivity(intent);
	}
	@Override
	public void initTitleBar() {
		super.initTitleBar();
		mTextTitleBack.setBackgroundResource(R.drawable.btn_back);
		mTextTitle.setText(R.string.title_register);
	}

	@OnClick({R.id.text_title_back, R.id.tb_send_validate_code, R.id.btn_confirm_register, R.id.tv_register_agreement,R.id.cb})
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.text_title_back:
				finish();
				break;
			case R.id.btn_confirm_register:
				if (mCbAgreeProtocol.isChecked()) {
					if (mEditTelephone.length() != 11) {
						ToolAlert.showToast(RegisterActivity.this, "请输入正确的手机号", false);
					} else if (!mEditPassword.getText().toString().equals(mEditRepeatpassword.getText().toString())) {
						ToolAlert.showToast(RegisterActivity.this, "密码输入不一致", false);
					} else if (mEditUsername.length() < 6 || mEditUsername.length() > 12) {
						ToolAlert.showToast(RegisterActivity.this, "用户名过于简单", false);
					} else if (mEditPassword.length() < 6) {
						ToolAlert.showToast(RegisterActivity.this, "密码过于简单，请重新输入", false);
					} else if (et_company.length() <= 0&&cb.isChecked()) {
						ToolAlert.showToast(RegisterActivity.this, "请填写所属公司", false);
					} else if (company.equals("请选择")&&!cb.isChecked()){
						ToolAlert.showToast(RegisterActivity.this, "请选择所属公司", false);
					}else {
						register();
					}
				} else {
					ToolAlert.showToast(RegisterActivity.this, "请确认注册协议", false);
				}

				break;
			case R.id.tb_send_validate_code:
				if (mEditTelephone.getText().toString().length() == 0 || mEditemail.getText().toString().length() == 0) {
					ToolAlert.showToast(RegisterActivity.this, "邮箱及手机号为必填项", false);
				} else {
					sendSmsCode();
				}
				break;
			case R.id.tv_register_agreement:
				AgreementActivity.startActivity(this);
				break;
			case R.id.cb:
				if (cb.isChecked()){
					et_company.setVisibility(View.VISIBLE);
				}else {
					et_company.setVisibility(View.GONE);
				}
				break;
		}
	}

	private void sendSmsCode() {
		Map map = new HashMap();
		map.put("phonenumber", mEditTelephone.getText().toString());
		map.put("email", mEditemail.getText().toString());
		String jsonStr = GsonUtils.mapToJson(map);
		Logger.i(TAG, jsonStr);
		try {
            OkHttpUtil.postJsonData2Server(RegisterActivity.this,
                    url_email,
                    jsonStr,
                    new OkHttpUtil.MyCallBack() {
                        @Override
                        public void onFailure(Request request, IOException e) {
                            ToolAlert.showToast(RegisterActivity.this, "服务器异常,请稍后再试", false);
                        }

                        @Override
                        public void onResponse(String json) {
//                                    Logger.i(TAG, json);
//                                    ServerResponse serverResponse = GsonUtils
//                                            .jsonToBean(json,
//                                                    ServerResponse.class
//                                            );
//                                    ToolAlert.showToast(RegisterActivity.this, serverResponse.getMessage(), false);
                        }
                    });
        } catch (IOException e) {
            e.printStackTrace();
        }
	}

	private void register() {
		Map map = new HashMap();
		map.put("username", mEditUsername.getText().toString());
		map.put("phonenumber", mEditTelephone.getText().toString());
		map.put("emailcode", mEditCode.getText().toString());
		map.put("password", mEditPassword.getText().toString());
		map.put("repeatpassword", mEditRepeatpassword.getText().toString());
		map.put("email", mEditemail.getText().toString());
		if (cb.isChecked()) {
            map.put("companyname", et_company.getText().toString());
        }else{
            map.put("companyname", company);
        }
		String jsonStr = GsonUtils.mapToJson(map);
		Logger.i(TAG, jsonStr);
		try {
            OkHttpUtil.postJsonData2Server(RegisterActivity.this,
                    url_register,
                    jsonStr,
                    new OkHttpUtil.MyCallBack() {
                        @Override
                        public void onFailure(Request request, IOException e) {
                            ToolAlert.showToast(RegisterActivity.this, "服务器异常,请稍后再试", false);
                        }

                        @Override
                        public void onResponse(String json) {
                            Logger.i(TAG, json);
                            ServerResponse serverResponse = GsonUtils
                                    .jsonToBean(json,
                                            ServerResponse.class
                                    );
                            ToolAlert.showToast(RegisterActivity.this, serverResponse.getMessage(), false);
                            if (!serverResponse.getStatus().equals("error")) {
                                finish();
                            }

                        }
                    });
        } catch (IOException e) {
            e.printStackTrace();
        }
	}


	@Override
	public void initView() {
		super.initView();
		mTbSendValidateCode.setTextAfter("s")
				.setTextBefore("发送验证码")
				.setLenght(60 * 1000)
				.setTextSize(14);
		//数据
		data_list = new ArrayList<String>();
		data_list.add("请选择");
		//适配器
		arr_adapter= new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, data_list);
		//设置样式
		arr_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		//加载适配器
		environment.setAdapter(arr_adapter);
		environment.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				company = data_list.get(position);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});
		try {
			OkHttpUtil.postJsonData2Server(RegisterActivity.this,
					url_companyname,
					"",
					new OkHttpUtil.MyCallBack() {
						@Override
						public void onFailure(Request request, IOException e) {
							ToolAlert.showToast(RegisterActivity.this, "连接服务器失败,请稍后再试", false);
						}

						@Override
						public void onResponse(String json) {
							Logger.i(TAG, json);
							CompanyInfo companyInfo = GsonUtils.jsonToBean(
									json, CompanyInfo.class
							);
							for (int i=0;i<companyInfo.getDocuments().size();i++){
								data_list.add(companyInfo.getDocuments().get(i).getCompanyname());
							}
							arr_adapter.notifyDataSetChanged();
						}
					}
			);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	@Override
	protected void onDestroy() {
		mTbSendValidateCode.onDestroy();
		super.onDestroy();
	}


}

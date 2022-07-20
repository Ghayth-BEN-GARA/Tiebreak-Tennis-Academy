package com.example.tiebreaktennisacademy.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.tiebreaktennisacademy.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ForgetPassword2Activity extends AppCompatActivity {

    private ImageView back;
    private AppCompatButton next;
    private TextInputEditText code1, code2, code3, code4, code5, code6;
    private TextInputLayout inputLayoutCode1, inputLayoutCode2, inputLayoutCode3, inputLayoutCode4, inputLayoutCode5, inputLayoutCode6;
    private TextView erreurCode;
    private Dialog dialog;
    private Boolean isCode1 = false, isCode2 = false, isCode3 = false, isCode4 = false, isCode5 = false, isCode6 = false;
    private String verificationId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password2);

        back = (ImageView) findViewById(R.id.back);
        next = (AppCompatButton) findViewById(R.id.next_btn);
        code1 = (TextInputEditText) findViewById(R.id.code1);
        code2 = (TextInputEditText) findViewById(R.id.code2);
        code3 = (TextInputEditText) findViewById(R.id.code3);
        code4 = (TextInputEditText) findViewById(R.id.code4);
        code5 = (TextInputEditText) findViewById(R.id.code5);
        code6 = (TextInputEditText) findViewById(R.id.code6);
        inputLayoutCode1 = (TextInputLayout) findViewById(R.id.input_code1);
        inputLayoutCode2 = (TextInputLayout) findViewById(R.id.input_code2);
        inputLayoutCode3 = (TextInputLayout) findViewById(R.id.input_code3);
        inputLayoutCode4 = (TextInputLayout) findViewById(R.id.input_code4);
        inputLayoutCode5 = (TextInputLayout) findViewById(R.id.input_code5);
        inputLayoutCode6 = (TextInputLayout) findViewById(R.id.input_code6);
        erreurCode = (TextView) findViewById(R.id.erreur_code_securite);

        getVerificationId();
        onclickFunctions();
        onChangeFunctions();
    }

    @Override
    public void onBackPressed() {
        ouvrirForgetPassword1Activity();
    }

    public void onclickFunctions(){
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ouvrirForgetPassword1Activity();
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateFormForgetPassword2();
            }
        });
    }

    public void validateFormForgetPassword2(){
        if(isEmpty(code1.getText().toString())){
            setErreurText(erreurCode,getString(R.string.code_required));
        }

        else if(isEmpty(code2.getText().toString())){
            setErreurText(erreurCode,getString(R.string.code_required));
        }

        else if(isEmpty(code3.getText().toString())){
            setErreurText(erreurCode,getString(R.string.code_required));
        }

        else if(isEmpty(code4.getText().toString())){
            setErreurText(erreurCode,getString(R.string.code_required));
        }

        else if(isEmpty(code5.getText().toString())){
            setErreurText(erreurCode,getString(R.string.code_required));
        }

        else if(isEmpty(code6.getText().toString())){
            setErreurText(erreurCode,getString(R.string.code_required));
        }

        else if(isCode1 == true && isCode2 == true && isCode3 == true && isCode4 == true && isCode5 == true && isCode6 == true){
            setErreurNull(erreurCode);
            testEgaliteCodeSecurite();
        }
    }

    public void ouvrirForgetPassword1Activity(){
        Intent intent = new Intent(getApplicationContext(), ForgetPassword1Activity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.left_to_right,R.anim.stay);
    }

    public void ouvrirForgetPassword3Activity(){
        Intent intent = new Intent(getApplicationContext(), ForgetPassword3Activity.class);
        intent.putExtra("email",getIntent().getStringExtra("email"));
        startActivity(intent);
        overridePendingTransition(R.anim.right_to_left,R.anim.stay);
    }

    public boolean isEmpty(String text){
        return text.isEmpty();
    }

    public boolean isNumber(String text) {
        Matcher matcher = Pattern.compile("^[0-9]*$").matcher(text);
        return matcher.matches();
    }

    public void setErreurNull(TextView text){
        text.setText(null);
    }

    public void setErreurText(TextView text, String message){
        text.setText(message);
    }

    public void onChangeFunctions(){
        code1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                validateCode1();
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!isEmpty(code1.getText().toString())){
                    swipeToOtherInput(code2);
                }
            }
        });

        code2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                validateCode2();
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!isEmpty(code2.getText().toString())){
                    swipeToOtherInput(code3);
                }
            }
        });

        code3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                validateCode3();
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!isEmpty(code3.getText().toString())){
                    swipeToOtherInput(code4);
                }
            }
        });

        code4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                validateCode4();
                if(!isEmpty(code4.getText().toString())){
                    swipeToOtherInput(code5);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        code5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                validateCode5();
                if(!isEmpty(code5.getText().toString())){
                    swipeToOtherInput(code6);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        code6.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                validateCode6();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }


    public void validateCode1(){
        if(isEmpty(code1.getText().toString())){
            setErreurText(erreurCode,getString(R.string.code_required));
            isCode1 = false;
        }

        else if(!isNumber(code1.getText().toString())){
            setErreurText(erreurCode,getString(R.string.code_number));
            isCode1 = false;
        }

        else{
            setErreurNull(erreurCode);
            isCode1 = true;
        }
    }

    public void validateCode2(){
        if(isEmpty(code2.getText().toString())){
            setErreurText(erreurCode,getString(R.string.code_required));
            isCode2 = false;
        }

        else if(!isNumber(code2.getText().toString())){
            setErreurText(erreurCode,getString(R.string.code_number));
            isCode2 = false;
        }

        else{
            setErreurNull(erreurCode);
            isCode2 = true;
        }
    }

    public void validateCode3(){
        if(isEmpty(code3.getText().toString())){
            setErreurText(erreurCode,getString(R.string.code_required));
            isCode3 = false;
        }

        else if(!isNumber(code3.getText().toString())){
            setErreurText(erreurCode,getString(R.string.code_number));
            isCode3 = false;
        }

        else{
            setErreurNull(erreurCode);
            isCode3 = true;
        }
    }

    public void validateCode4(){
        if(isEmpty(code4.getText().toString())){
            setErreurText(erreurCode,getString(R.string.code_required));
            isCode4 = false;
        }

        else if(!isNumber(code4.getText().toString())){
            setErreurText(erreurCode,getString(R.string.code_number));
            isCode4 = false;
        }

        else{
            setErreurNull(erreurCode);
            isCode4 = true;
        }
    }

    public void validateCode5(){
        if(isEmpty(code4.getText().toString())){
            setErreurText(erreurCode,getString(R.string.code_required));
            isCode5 = false;
        }

        else if(!isNumber(code5.getText().toString())){
            setErreurText(erreurCode,getString(R.string.code_number));
            isCode5 = false;
        }

        else{
            setErreurNull(erreurCode);
            isCode5 = true;
        }
    }

    public void validateCode6(){
        if(isEmpty(code6.getText().toString())){
            setErreurText(erreurCode,getString(R.string.code_required));
            isCode6 = false;
        }

        else if(!isNumber(code4.getText().toString())){
            setErreurText(erreurCode,getString(R.string.code_number));
            isCode6 = false;
        }

        else{
            setErreurNull(erreurCode);
            isCode6 = true;
        }
    }

    public void swipeToOtherInput(TextInputEditText text){
        text.requestFocus();
    }

    public void getVerificationId(){
        verificationId = getIntent().getStringExtra("verificationId");
    }

    public void testEgaliteCodeSecurite(){
        String codeSaisie = code1.getText().toString() + code2.getText().toString() + code3.getText().toString() +
                            code4.getText().toString() + code5.getText().toString() + code6.getText().toString();

        final ProgressDialog progressDialog = new ProgressDialog(ForgetPassword2Activity.this, R.style.chargement);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage(getString(R.string.verification_progress));
        progressDialog.show();

        new Thread(new Runnable() {
            public void run() {
                checkIfCodeIsCorrect(progressDialog,codeSaisie);
            }
        }).start();
    }

    public void checkIfCodeIsCorrect(ProgressDialog progressDialog, String code){
        if(verificationId != null){
            PhoneAuthCredential phoneAuthCredential = PhoneAuthProvider.getCredential(verificationId,code);

            FirebaseAuth.getInstance().signInWithCredential(phoneAuthCredential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        progressDialog.dismiss();
                        showNotificationSuccess();
                    }

                    else{
                        progressDialog.dismiss();
                        setErreurText(erreurCode,getString(R.string.error_code));
                    }
                }
            });
        }
    }

    public void showNotificationSuccess(){
        dialog = new Dialog(ForgetPassword2Activity.this);
        dialog.setContentView(R.layout.item_success);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCanceledOnTouchOutside(false);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            dialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.content_erreur_notification));
        }

        AppCompatButton cancel = dialog.findViewById(R.id.exit_btn);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                ouvrirForgetPassword3Activity();
            }
        });

        TextView desc = dialog.findViewById(R.id.desc_title_success);
        desc.setText(R.string.account_getted_desc);

        TextView title = dialog.findViewById(R.id.title_success);
        title.setText(getString(R.string.account_getted));

        dialog.show();
    }
}
package com.pesh.mentalcare.ui.login

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import android.view.inputmethod.EditorInfo
import android.widget.*
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.pesh.mentalcare.R
import com.pesh.mentalcare.fragments.IllnessesTypesFragment
import com.pesh.mentalcare.model.UserModel
import java.util.*

class LoginFragment : Fragment() {

    private lateinit var loginViewModel: LoginViewModel


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.fragment_login, container, false)


    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loginViewModel = ViewModelProvider(this, LoginViewModelFactory())
            .get(LoginViewModel::class.java)

        val usernameEdtLogin = view.findViewById<EditText>(R.id.edtLoginUserName)
        val passwordEdtLogin = view.findViewById<EditText>(R.id.edtLoginPassword)
        val loginButton = view.findViewById<Button>(R.id.btnLogin)
        val loadingProgressBar = view.findViewById<ProgressBar>(R.id.loading)
        val txtSignUp = view.findViewById<TextView>(R.id.txtSignUp)

        loginViewModel.loginFormState.observe(viewLifecycleOwner,
            Observer { loginFormState ->
                if (loginFormState == null) {
                    return@Observer
                }
                loginButton.isEnabled = loginFormState.isDataValid
                loginFormState.usernameError?.let {
                    usernameEdtLogin.error = getString(it)
                }
                loginFormState.passwordError?.let {
                    passwordEdtLogin.error = getString(it)
                }
            })

        loginViewModel.loginResult.observe(viewLifecycleOwner,
            Observer { loginResult ->
                loginResult ?: return@Observer
                loadingProgressBar.visibility = View.GONE
                loginResult.error?.let {
                    showLoginFailed(it)
                }
                loginResult.success?.let {
                    updateUiWithUser(it)
                }
            })

        val afterTextChangedListener = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                // ignore
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                // ignore
            }

            override fun afterTextChanged(s: Editable) {
                loginViewModel.loginDataChanged(
                    usernameEdtLogin.text.toString(),
                    passwordEdtLogin.text.toString()
                )
            }
        }
        usernameEdtLogin.addTextChangedListener(afterTextChangedListener)
        passwordEdtLogin.addTextChangedListener(afterTextChangedListener)
        passwordEdtLogin.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                loginViewModel.login(
                    usernameEdtLogin.text.toString(),
                    passwordEdtLogin.text.toString()
                )
            }
            false
        }

        loginButton.setOnClickListener {
            authenticate(
                usernameEdtLogin.text.toString(),
                passwordEdtLogin.text.toString()
            )
//            )
        }

        txtSignUp.setOnClickListener {
            openRegistrationForm()
        }
    }

    private fun authenticate(email: String, password: String) {
        if (!(email.isEmpty() || password.isEmpty())) {
            val waitingDialog = loadingDialog();
            waitingDialog?.show()
            val auth = FirebaseAuth.getInstance();
            auth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
                if (it.isSuccessful) {
                    waitingDialog?.dismiss()
                    val fragment = IllnessesTypesFragment()
                    val fragmentManager = activity?.supportFragmentManager
                    val transaction = fragmentManager?.beginTransaction()
                    transaction?.replace(R.id.myNavHostFragment, fragment)
                    transaction?.commit()

                } else {
                    waitingDialog?.dismiss()
                    Toast.makeText(context, it.exception?.localizedMessage, Toast.LENGTH_LONG).show()
                }
            }
        } else {
            val fragment = IllnessesTypesFragment()
            val fragmentManager = activity?.supportFragmentManager
            val transaction = fragmentManager?.beginTransaction()
            transaction?.replace(R.id.myNavHostFragment, fragment)
            transaction?.commit()
            Toast.makeText(context, "All fields required", Toast.LENGTH_LONG).show()
        }
    }

    @SuppressLint("InflateParams")
    private fun openRegistrationForm() {
        val root = LayoutInflater.from(context).inflate(R.layout.registration_form, null)
        val edName = root.findViewById<TextInputEditText>(R.id.edName)
        val edEmail = root.findViewById<TextInputEditText>(R.id.edEmail)
        val edPassword = root.findViewById<TextInputEditText>(R.id.edPassword)
        val edConfirmPassword = root.findViewById<TextInputEditText>(R.id.edConfirmPassword)
        val btnSignUp = root.findViewById<Button>(R.id.btnSignUp);

        val registrationDialog = context?.let {
            AlertDialog.Builder(it)
                .setView(root)
                .create()
        }
        registrationDialog?.show()

        btnSignUp.setOnClickListener {
            if (!(edName.text.isNullOrEmpty() || edEmail.text.isNullOrEmpty() ||
                        edPassword.text.isNullOrEmpty() || edConfirmPassword.text.isNullOrEmpty())
            ) {
                if (edPassword.text.toString() == edConfirmPassword.text.toString()) {
                    val waitingDialog = loadingDialog();
                    waitingDialog?.show()
                    val id = Date().time.toString()
                    val user = UserModel(id, edName.text.toString(), edEmail.text.toString())
                    val auth = FirebaseAuth.getInstance();
                    auth.createUserWithEmailAndPassword(
                        edEmail.text.toString(),
                        edPassword.text.toString()
                    ).addOnCompleteListener { createAccountTask ->
                        if (createAccountTask.isSuccessful) {
                            val dbReference = FirebaseDatabase.getInstance().getReference("Users")
                            dbReference.setValue(user).addOnCompleteListener { insertTask ->
                                if (insertTask.isSuccessful) {
                                    registrationDialog?.dismiss()
                                    Toast.makeText(
                                        context,
                                        "Registration Successful",
                                        Toast.LENGTH_LONG
                                    ).show()
                                    waitingDialog?.dismiss()
                                } else {
                                    Toast.makeText(
                                        context,
                                        insertTask.exception?.localizedMessage,
                                        Toast.LENGTH_LONG
                                    ).show()
                                    waitingDialog?.dismiss()
                                }
                            }
                        } else {
                            Toast.makeText(
                                context,
                                createAccountTask.exception?.localizedMessage,
                                Toast.LENGTH_LONG
                            )
                                .show()
                            waitingDialog?.dismiss()
                        }
                    }

                } else {
                    Toast.makeText(context, "Passwords do not match", Toast.LENGTH_LONG).show()
                }

            } else {
                Toast.makeText(context, "All fields required", Toast.LENGTH_LONG).show()
            }
        }

    }


    private fun updateUiWithUser(model: LoggedInUserView) {
        val welcome = getString(R.string.welcome) + model.displayName
        // TODO : initiate successful logged in experience
        val appContext = context?.applicationContext ?: return
        Toast.makeText(appContext, welcome, Toast.LENGTH_LONG).show()
    }

    private fun showLoginFailed(@StringRes errorString: Int) {
        val appContext = context?.applicationContext ?: return
        Toast.makeText(appContext, errorString, Toast.LENGTH_LONG).show()
    }

    private fun loadingDialog(): AlertDialog? {
        val root = LayoutInflater.from(context).inflate(R.layout.waiting_dialog_item, null)

        val waitingDialog = context?.let {
            AlertDialog.Builder(it)
                .setView(root)
                .setCancelable(false)
                .create()
        }
        return waitingDialog;
    }
}

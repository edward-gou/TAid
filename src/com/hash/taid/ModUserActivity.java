package com.hash.taid;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.hash.taid.customs.TAidActivity;

public class ModUserActivity extends TAidActivity {

	EditText name, username, pass, server, port, sport;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mod_user);

		name = (EditText) findViewById(R.id.name);
		username = (EditText) findViewById(R.id.username);
		pass = (EditText) findViewById(R.id.password);
		server = (EditText) findViewById(R.id.server);
		port = (EditText) findViewById(R.id.port);
		sport = (EditText) findViewById(R.id.sockport);

		if (user.getName() != null && !user.getName().isEmpty()) {
			name.setText(user.getName());
		}

		if (user.getUsername() != null && !user.getUsername().isEmpty()) {
			username.setText(user.getUsername());
		}

		if (user.getPassword() != null && !user.getPassword().isEmpty()) {
			pass.setText(user.getPassword());
		}

		if (user.getSmtpServer() != null && !user.getSmtpServer().isEmpty()) {
			server.setText(user.getSmtpServer());
		}

		if (user.getSmtpPort() != null && !user.getSmtpPort().isEmpty()) {
			port.setText(user.getSmtpPort());
		}

		if (user.getSocketPort() != null && !user.getSocketPort().isEmpty()) {
			sport.setText(user.getSocketPort());
		}
	}

	public void saveButton(View view) {

		user.setName(name.getText().toString());
		user.setUsername(username.getText().toString());
		user.setPassword(pass.getText().toString());
		user.setSmtpServer(server.getText().toString());
		user.setSmtpPort(port.getText().toString());
		user.setSocketPort(sport.getText().toString());

		onBackPressed();
	}

	public void cancelButton(View view) {
		onBackPressed();
	}

}

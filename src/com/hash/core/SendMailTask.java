package com.hash.core;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

public class SendMailTask extends AsyncTask<Object, Void, Boolean> {

    private Exception exception;
    private boolean fields;
    private Object[] mailData;
    
    protected Boolean doInBackground(Object... md) {
		try {
    		MailManager mail = new MailManager();
    		this.mailData = md;
    		//Apply data to the mail
    		mail.setFrom((String)mailData[1]);
    		mail.setLogin((String)mailData[2], (String)mailData[3]);
    		mail.setSmtpServer((String)mailData[4]);
    		mail.setSmtpPort((String)mailData[5]);
    		mail.setSocketPort((String)mailData[6]);
    		mail.setTo((String)mailData[7]);
    		mail.setSub((String)mailData[8]);
    		mail.setBody((String)mailData[9]);
    		mail.addAttachment((String)mailData[10]);
    		//Send mail
			mail.send();
	    	return true;
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
    }
    
    @Override
    protected void onPostExecute(Boolean result)
    {
    	//Toast the result of the task
    	if (result == false)
    	{
    		Toast.makeText((Context)mailData[0], "Send failed. Could not connect to mail host. Check your settings.",
					Toast.LENGTH_SHORT).show();
    	}
    	else
    	{
    		Toast.makeText((Context)mailData[0], "Marks sent successfully.",
					Toast.LENGTH_SHORT).show();
    	}
    }
}
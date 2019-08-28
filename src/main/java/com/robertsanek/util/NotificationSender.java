package com.robertsanek.util;

import java.io.IOException;

import com.github.sheigutn.pushbullet.Pushbullet;
import com.github.sheigutn.pushbullet.items.push.sendable.defaults.SendableNotePush;
import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import com.twilio.Twilio;

import net.pushover.client.MessagePriority;
import net.pushover.client.PushoverClient;
import net.pushover.client.PushoverMessage;
import net.pushover.client.PushoverRestClient;

public class NotificationSender {

  private static final Log log = Logs.getLog(NotificationSender.class);
  private static final Pushbullet pushbullet =
      new Pushbullet(CommonProvider.getSecret(SecretType.PUSHBULLET_ACCESS_TOKEN));
  private static PushoverClient client = new PushoverRestClient();

  //Sends notification using default provider
  public boolean sendNotificationDefault(String title, String message) {
    return sendEmailDefault(title, message) &&
        (sendPushBulletNotification(title, message)
            || sendPushoverNotification(title, message)
            || sendTwilioNotification(title, message));
  }

  public boolean sendEmailDefault(String title, String message) {
    return sendEmail(new Email("notifications@robertsanek.com", "Notification Service"),
        new Email(CommonProvider.getEmailAddress()), title, new Content("text/plain", message));
  }

  public boolean sendEmail(Email from, Email to, String subject, Content content) {
    Mail mail = new Mail(from, subject, to, content);
    SendGrid sg = new SendGrid(CommonProvider.getSecret(SecretType.SENDGRID_API_KEY));
    Request request = new Request();
    try {
      request.setMethod(Method.POST);
      request.setEndpoint("mail/send");
      request.setBody(mail.build());
      Response response = sg.api(request);
      if (response.getStatusCode() >= 300 || response.getStatusCode() < 200) {
        log.error("SendGrid returned non-success response: %s", response.getBody());
      } else {
        return true;
      }
    } catch (IOException ex) {
      log.error("Failed to send e-mail. Exception was:");
      log.error(ex);
    }
    return false;
  }

  public boolean sendTwilioNotification(String title, String message) {
    Twilio.init(CommonProvider.getSecret(SecretType.TWILIO_ACCOUNT_SID),
        CommonProvider.getSecret(SecretType.TWILIO_AUTH_TOKEN));
    //would require me to purchase a phone number
    return false;
  }

  public boolean sendPushoverNotification(String title, String message) {
    try {
      Unchecked.run(() -> client
          .pushMessage(PushoverMessage.builderWithApiToken(CommonProvider.getSecret(SecretType.PUSHOVER_API_TOKEN))
              .setUserId(CommonProvider.getSecret(SecretType.PUSHOVER_USER_ID))
              .setTitle(title)
              .setMessage(message)
              .setPriority(MessagePriority.HIGH)
              .build()));
      log.info("Sent push notification via Pushover with title \"%s\" to user \"%s\".", title,
          CommonProvider.getSecret(SecretType.PUSHOVER_USER_ID));
    } catch (Exception e) {
      log.error(e);
      return false;
    }
    return true;
  }

  public boolean sendPushBulletNotification(String title, String message) {
    try {
      pushbullet.push(new SendableNotePush(title, message));
      log.info("Sent push notification via PushBullet with title \"%s\" to all clients.", title);
    } catch (Exception e) {
      log.error(e);
      return false;
    }
    return true;
  }

}

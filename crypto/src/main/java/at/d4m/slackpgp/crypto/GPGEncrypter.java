/*
 *     SlackGPG
 *     Copyright (C) 2017 Christoph Muck
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package at.d4m.slackpgp.crypto;

import com.google.common.io.CharStreams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Christoph Muck
 */
public class GPGEncrypter {

    private static final Logger LOG = LoggerFactory.getLogger(GPGEncrypter.class);

    @Inject
    public GPGEncrypter() {
    }

    public String encryptMessage(String message, List<String> emailsOfRecipients) {
        try {
            LOG.info("Trying to enrcypt message: '{}' with recipients : {}", message, emailsOfRecipients);
            return doEncryptMessage(message, emailsOfRecipients);
        } catch (InterruptedException | IOException e) {
            LOG.warn("Error encrypting message " + message, e);
            return null;
        }
    }

    private String doEncryptMessage(String message, List<String> emailsOfRecipients) throws InterruptedException, IOException {
        ArrayList<String> gpgArgs = new ArrayList<>(2 + (emailsOfRecipients.size() * 2));
        gpgArgs.add("gpg2");
        gpgArgs.add("-eas");
        for (String email : emailsOfRecipients) {
            gpgArgs.add("-r");
            gpgArgs.add(email);
        }

        ProcessBuilder gpgProccessBuilder = new ProcessBuilder(gpgArgs.toArray(new String[0]));
        Process gpgProcess = gpgProccessBuilder.start();

        OutputStream stdin = gpgProcess.getOutputStream();

        try (PrintWriter printWriter = new PrintWriter(stdin)) {
            printWriter.write(message);
        }

        gpgProcess.waitFor();

        try (InputStreamReader gpgOutput = new InputStreamReader(gpgProcess.getInputStream(), "UTF-8")) {
            return CharStreams.toString(gpgOutput);
        }
    }

    public String decryptMessage(String encryptedMessage) {
        try {
            LOG.info("Trying to decrypt message: '{}'", encryptedMessage);
            return doDecryptMessage(encryptedMessage);
        } catch (InterruptedException | IOException e) {
            LOG.warn("Error decrypting message " + encryptedMessage, e);
            return null;
        }
    }

    private String doDecryptMessage(String encryptedMessage) throws InterruptedException, IOException {
        ProcessBuilder gpgProccessBuilder = new ProcessBuilder("gpg2", "-d");
        Process gpgProcess = gpgProccessBuilder.start();

        OutputStream stdin = gpgProcess.getOutputStream();

        try (PrintWriter printWriter = new PrintWriter(stdin)) {
            printWriter.write(encryptedMessage);
        }

        gpgProcess.waitFor();

        try (InputStreamReader gpgOutput = new InputStreamReader(gpgProcess.getInputStream(), "UTF-8")) {
            return CharStreams.toString(gpgOutput);
        }
    }
}

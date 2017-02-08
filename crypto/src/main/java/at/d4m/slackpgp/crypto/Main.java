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

import com.google.common.collect.ImmutableList;
import org.bouncycastle.openpgp.PGPException;

import java.io.IOException;
import java.util.List;

/**
 * @author Christoph Muck
 */
public class Main {
    public static void main(String[] args) throws IOException, PGPException, InterruptedException {
        String test = "HALLIHALLO!";
        List<String> email = ImmutableList.of("muckz11@gmail.com", "kai.takac@gmail.com");

        GPGEncrypter gpgEncrypter = new GPGEncrypter();
        String encryptedMessage = gpgEncrypter.encryptMessage(test, email);
        System.out.println(encryptedMessage);
        System.out.println(gpgEncrypter.decryptMessage(encryptedMessage));
    }

}

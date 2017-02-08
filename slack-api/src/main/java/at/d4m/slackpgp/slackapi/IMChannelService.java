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

package at.d4m.slackpgp.slackapi;

import allbegray.slack.type.DirectMessageChannel;
import allbegray.slack.type.History;
import allbegray.slack.type.Message;
import allbegray.slack.type.User;
import allbegray.slack.webapi.SlackWebApiClient;
import at.d4m.slackpgp.crypto.GPGEncrypter;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import javax.inject.Inject;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Christoph Muck
 */
public class IMChannelService {

    private final SlackWebApiClient slackWebApiClient;
    private final GPGEncrypter gpgEncrypter;

    @Inject
    public IMChannelService(SlackWebApiClient slackWebApiClient, GPGEncrypter gpgEncrypter) {
        this.slackWebApiClient = slackWebApiClient;
        this.gpgEncrypter = gpgEncrypter;
    }

    public List<IMChannel> getAllIMChannels() {
        List<DirectMessageChannel> imChannelList = slackWebApiClient.getDirectMessageChannelList();
        List<User> userList = slackWebApiClient.getUserList();

        ImmutableList.Builder<IMChannel> channelsBuilder = ImmutableList.builder();
        for (DirectMessageChannel directMessageChannel : imChannelList) {
            if (!directMessageChannel.getIs_user_deleted()) {
                User matchingUser = userList.stream().filter(user -> user.getId().equals(directMessageChannel.getUser())).findFirst().orElse(null);
                channelsBuilder.add(new IMChannel(directMessageChannel.getId(), matchingUser));
            }
        }
        return channelsBuilder.build();
    }

    public List<String> getMessageForChannel(IMChannel imChannel) {
        History directMessageChannelHistory = slackWebApiClient.getDirectMessageChannelHistory(imChannel.getId(), 10);
        List<Message> messages = directMessageChannelHistory.getMessages();
        return Lists.reverse(messages.stream().map((message) -> {
            String text = message.getText();
            if (text.startsWith("-----BEGIN PGP MESSAGE-----")) {
                text = gpgEncrypter.decryptMessage(text);
            }
            return text;
        }).collect(Collectors.toList()));
    }

    public void sendMessageToChannel(IMChannel imChannel, String message) {
        ImmutableList<String> of = ImmutableList.of("muckz11@gmail.com", imChannel.getUser().getProfile().getEmail());
        String encryptedMessage = gpgEncrypter.encryptMessage(message, of);
        slackWebApiClient.meMessage(imChannel.getId(), encryptedMessage);
    }
}

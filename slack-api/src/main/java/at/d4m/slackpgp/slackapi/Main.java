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

import allbegray.slack.SlackClientFactory;
import allbegray.slack.rtm.Event;
import allbegray.slack.rtm.EventListener;
import allbegray.slack.rtm.SlackRealTimeMessagingClient;
import allbegray.slack.type.DirectMessageChannel;
import allbegray.slack.type.User;
import allbegray.slack.webapi.SlackWebApiClient;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Christoph Muck
 */
public class Main {

    private static final String TOKEN = "";

    public static void main(String[] args) {
        ExecutorService executorService = Executors.newSingleThreadExecutor();

        SlackRealTimeMessagingClient rtmClient = SlackClientFactory
                .createSlackRealTimeMessagingClient(TOKEN);

        rtmClient.addListener(Event.MESSAGE, new EventListener() {
            @Override
            public void handleMessage(JsonNode jsonNode) {
                System.out.println(jsonNode);
            }
        });

        executorService.execute(new Runnable() {
            @Override
            public void run() {
                rtmClient.connect();
            }
        });

        SlackWebApiClient webApiClient = SlackClientFactory.createWebApiClient(TOKEN);

        List<DirectMessageChannel> channelList = webApiClient.getDirectMessageChannelList();
        channelList.forEach(System.out::println);

        List<User> userList = webApiClient.getUserList();
        userList.forEach(System.out::println);

//        GPGEncrypter gpgEncrypter = new GPGEncrypter();
//        List<String> emails = ImmutableList.of("kai.takac@gmail.com", "muckz11@gmail.com");
//        String message = "Hello ;)";

//        String encryptedMessage = gpgEncrypter.encryptMessage(message, emails);

//        webApiClient.meMessage("D0RT617NZ", encryptedMessage);
    }
}

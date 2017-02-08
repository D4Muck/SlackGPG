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
import allbegray.slack.rtm.SlackRealTimeMessagingClient;
import allbegray.slack.webapi.SlackWebApiClient;
import dagger.Module;
import dagger.Provides;

import javax.inject.Singleton;

/**
 * @author Christoph Muck
 */
@Module
public class SlackApiModule {

    private static final String TOKEN = "";

    public SlackApiModule() {
    }

    @Singleton
    @Provides
    public SlackRealTimeMessagingClient provideSlackRealTimeMessagingClient() {
        return SlackClientFactory.createSlackRealTimeMessagingClient(TOKEN);
    }

    @Singleton
    @Provides
    public SlackWebApiClient provideSlackWebApiClient() {
        return SlackClientFactory.createWebApiClient(TOKEN);
    }
}

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

package at.d4m.slackpgp.gui;

import at.d4m.slackpgp.slackapi.DaggerSlackApiComponent;
import at.d4m.slackpgp.slackapi.IMChannel;
import at.d4m.slackpgp.slackapi.IMChannelService;
import at.d4m.slackpgp.slackapi.SlackApiComponent;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;

/**
 * @author Christoph Muck
 */
public class Test {

    private JPanel panel;
    private JList<String> list1;
    private JList<String> list2;
    private JTextArea textArea1;

    public Test() {
        SlackApiComponent slackApiComponent = DaggerSlackApiComponent.builder().build();
        IMChannelService imChannelService = slackApiComponent.imChannelService();
        List<IMChannel> imChannels = imChannelService.getAllIMChannels();

        DefaultListModel<String> model = new DefaultListModel<>();
        list1.setModel(model);

        DefaultListModel<String> model2 = new DefaultListModel<>();
        list2.setModel(model2);

        for (IMChannel imChannel : imChannels) {
            model.addElement(imChannel.getUser().getName());
        }
        list1.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent listSelectionEvent) {
                if (listSelectionEvent.getValueIsAdjusting()) return;

                int selectedIndex = list1.getSelectedIndex();
                List<String> messageForChannel = imChannelService.getMessageForChannel(imChannels.get(selectedIndex));

                model2.removeAllElements();
                messageForChannel.forEach(model2::addElement);
            }
        });
        textArea1.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    imChannelService.sendMessageToChannel(imChannels.get(list1.getSelectedIndex()), textArea1.getText());
                    textArea1.setText("");
                }
            }
        });
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Test");
        frame.setContentPane(new Test().panel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}

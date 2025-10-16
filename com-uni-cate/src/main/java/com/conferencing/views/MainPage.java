package com.conferencing.views;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.conferencing.App;
import com.conferencing.theme.Theme;
import com.conferencing.theme.ThemeManager;
import com.conferencing.ui.CustomButton;
import com.conferencing.ui.PlaceholderTextField;
import com.conferencing.ui.ThemeToggleButton;
import com.conferencing.ui.TitledPanel;

public class MainPage extends JPanel {

    private final App app;
    private JLabel userInitialLabel;
    private JPanel headerPanel;
    private JPanel centerPanel;
    private JPanel meetingControlsPanel;
    private JPanel themePanelWrapper;

    public MainPage(App app) {
        this.app = app;
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        // Header
        headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBorder(new EmptyBorder(15, 25, 15, 25));

        JLabel logoLabel = new JLabel("Comm-Uni-Cate");
        logoLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        headerPanel.add(logoLabel, BorderLayout.WEST);

        JLabel dateTimeLabel = new JLabel(new SimpleDateFormat("HH:mm, E, MMM d").format(new Date()));
        dateTimeLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        headerPanel.add(dateTimeLabel, BorderLayout.CENTER);
        dateTimeLabel.setHorizontalAlignment(JLabel.CENTER);

        userInitialLabel = new JLabel("Y") {
             @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillOval(0, 0, getWidth(), getHeight());
                super.paintComponent(g);
                g2.dispose();
            }
        };
        userInitialLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        userInitialLabel.setHorizontalAlignment(JLabel.CENTER);
        userInitialLabel.setVerticalAlignment(JLabel.CENTER);
        userInitialLabel.setPreferredSize(new Dimension(40, 40));
        userInitialLabel.setOpaque(false);
        headerPanel.add(userInitialLabel, BorderLayout.EAST);
        add(headerPanel, BorderLayout.NORTH);

        // Center Panel
        centerPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 0, 10, 0);

        JLabel appTitle = new JLabel("Comm-Uni-Cate");
        appTitle.setFont(new Font("SansSerif", Font.BOLD, 48));
        appTitle.setHorizontalAlignment(JLabel.CENTER);
        centerPanel.add(appTitle, gbc);

        JLabel subtitle = new JLabel("Start or join a video meeting");
        subtitle.setFont(new Font("SansSerif", Font.PLAIN, 18));
        subtitle.setHorizontalAlignment(JLabel.CENTER);
        centerPanel.add(subtitle, gbc);
        
        centerPanel.add(Box.createRigidArea(new Dimension(0, 50)), gbc);

        // Meeting controls
        meetingControlsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        CustomButton newMeetingButton = new CustomButton("New Meeting", true);
        newMeetingButton.setPreferredSize(new Dimension(150, 45));
        newMeetingButton.addActionListener(e -> app.showPage(App.MEETING_PAGE));
        
        PlaceholderTextField meetingCodeField = new PlaceholderTextField("Enter meeting code");
        meetingCodeField.setPreferredSize(new Dimension(250, 45));

        CustomButton joinButton = new CustomButton("Join", true);
        joinButton.setPreferredSize(new Dimension(120, 45));
        joinButton.addActionListener(e -> {
            if (!meetingCodeField.getText().trim().isEmpty()) {
                app.showPage(App.MEETING_PAGE);
            }
        });

        meetingControlsPanel.add(newMeetingButton);
        meetingControlsPanel.add(meetingCodeField);
        meetingControlsPanel.add(joinButton);
        centerPanel.add(meetingControlsPanel, gbc);

        // Theme Toggle Panel
        TitledPanel themePanel = new TitledPanel();
        themePanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 15));
        JLabel changeThemeLabel = new JLabel("Theme:");
        changeThemeLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        
        themePanel.add(changeThemeLabel);
        themePanel.add(new ThemeToggleButton());
        
        themePanelWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER));
        themePanel.setPreferredSize(new Dimension(200, 70));
        themePanelWrapper.add(themePanel);
        
        gbc.gridy = 4;
        gbc.insets = new Insets(50, 0, 10, 0);
        gbc.anchor = GridBagConstraints.CENTER;
        centerPanel.add(themePanelWrapper, gbc);

        add(centerPanel, BorderLayout.CENTER);
        
        applyTheme();
    }
    
    private void applyTheme() {
        Theme theme = ThemeManager.getInstance().getTheme();
        setBackground(theme.getBackground());
        if (headerPanel != null) {
            headerPanel.setBackground(theme.getBackground());
        }
        if (centerPanel != null) {
            centerPanel.setBackground(theme.getBackground());
        }
        if (meetingControlsPanel != null) {
            meetingControlsPanel.setBackground(theme.getBackground());
        }
        if (themePanelWrapper != null) {
            themePanelWrapper.setBackground(theme.getBackground());
        }
        if (userInitialLabel != null) {
            userInitialLabel.setBackground(theme.getPrimary());
            userInitialLabel.setForeground(Color.WHITE);
        }
    }
    
    @Override
    public void updateUI() {
        super.updateUI();
        if (ThemeManager.getInstance() != null) {
            applyTheme();
        }
    }
}


package com.swe.controller.ux;

import com.swe.controller.Meeting.MeetingSession;
import com.swe.controller.Meeting.SessionMode;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

/**
 * View for displaying a list of meeting participants.
 * Uses MVVM pattern with ParticipantsListViewModel.
 */
public class ParticipantsListView extends JPanel {
    
    private static final int LAYOUT_GAP = 10;
    private static final int TITLE_FONT_SIZE = 20;
    private static final int BUTTON_FONT_SIZE = 14;
    private static final int NAME_FONT_SIZE = 15;
    private static final int DETAIL_FONT_SIZE = 12;
    private static final int ROW_HEIGHT = 70;
    
    private static final Color BG_COLOR = new Color(250, 252, 255);
    private static final Color ACCENT_COLOR = new Color(0, 120, 215);
    private static final Color LIST_BG_COLOR = new Color(245, 247, 250);
    private static final Color SELECTION_COLOR = new Color(225, 235, 255);
    
    private final ParticipantsListViewModel viewModel;
    private final DefaultListModel<Participant> listModel;
    private final JList<Participant> participantList;
    private final JLabel countLabel;
    
    /**
     * Creates a new ParticipantsListView.
     *
     * @param viewModel The view model to use
     */
    public ParticipantsListView(final ParticipantsListViewModel viewModel) {
        super(new BorderLayout(LAYOUT_GAP, LAYOUT_GAP));
        this.viewModel = viewModel;
        this.listModel = new DefaultListModel<>();
        
        setBackground(BG_COLOR);
        setBorder(new EmptyBorder(15, 15, 15, 15));
        
        // Title Panel
        JPanel titlePanel = createTitlePanel();
        add(titlePanel, BorderLayout.NORTH);
        
        // Participant List
        participantList = new JList<>(listModel);
        participantList.setCellRenderer(new ParticipantCellRenderer());
        participantList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        participantList.setBackground(LIST_BG_COLOR);
        participantList.setFixedCellHeight(ROW_HEIGHT);
        
        JScrollPane scrollPane = new JScrollPane(participantList);
        scrollPane.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollPane, BorderLayout.CENTER);
        
        // Count Label
        countLabel = new JLabel();
        countLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        countLabel.setForeground(Color.DARK_GRAY);
        updateCountLabel();
        
        // Bottom Panel
        JPanel bottomPanel = createBottomPanel();
        add(bottomPanel, BorderLayout.SOUTH);
        
        // Set up ViewModel listener
        setupViewModelBindings();
        
        // Load initial data
        refreshParticipantList();
    }
    
    /**
     * Creates the title panel.
     *
     * @return The title panel
     */
    private JPanel createTitlePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        
        JLabel titleLabel = new JLabel("Meeting Participants");
        titleLabel.setFont(new Font("Segoe UI Semibold", Font.BOLD, 
                TITLE_FONT_SIZE));
        titleLabel.setForeground(ACCENT_COLOR);
        panel.add(titleLabel, BorderLayout.WEST);
        
        return panel;
    }
    
    /**
     * Creates the bottom panel with count and buttons.
     *
     * @return The bottom panel
     */
    private JPanel createBottomPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(10, 0, 0, 0));
        
        // Count label on left
        panel.add(countLabel, BorderLayout.WEST);
        
        // Buttons on right
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonPanel.setOpaque(false);
        
        JButton refreshButton = createStyledButton("Refresh");
        refreshButton.addActionListener(e -> refreshParticipantList());
        buttonPanel.add(refreshButton);
        
        panel.add(buttonPanel, BorderLayout.EAST);
        
        return panel;
    }
    
    /**
     * Creates a styled button.
     *
     * @param text The button text
     * @return The styled button
     */
    private JButton createStyledButton(final String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.PLAIN, BUTTON_FONT_SIZE));
        button.setBackground(ACCENT_COLOR);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(true);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }
    
    /**
     * Sets up bindings with the ViewModel.
     */
    private void setupViewModelBindings() {
        viewModel.addListener(participants -> 
            SwingUtilities.invokeLater(this::refreshParticipantList)
        );
    }
    
    /**
     * Refreshes the participant list from the ViewModel.
     */
    private void refreshParticipantList() {
        listModel.clear();
        List<Participant> participants = viewModel.getParticipants();
        for (Participant participant : participants) {
            listModel.addElement(participant);
        }
        updateCountLabel();
    }
    
    /**
     * Updates the count label.
     */
    private void updateCountLabel() {
        int total = viewModel.getParticipantCount();
        int instructors = viewModel.getInstructorCount();
        int students = viewModel.getStudentCount();
        countLabel.setText(String.format(
            "Total: %d | Instructors: %d | Students: %d", 
            total, instructors, students
        ));
    }
    
    /**
     * Custom cell renderer for participants.
     */
    private static class ParticipantCellRenderer 
            implements ListCellRenderer<Participant> {
        
        @Override
        public Component getListCellRendererComponent(
                final JList<? extends Participant> list,
                final Participant participant,
                final int index,
                final boolean isSelected,
                final boolean cellHasFocus) {
            
            JPanel panel = new JPanel(new BorderLayout(10, 0));
            panel.setBorder(new EmptyBorder(10, 15, 10, 15));
            panel.setBackground(isSelected ? SELECTION_COLOR : LIST_BG_COLOR);
            
            // Profile Image
            JLabel picLabel = createProfileImageLabel(participant);
            panel.add(picLabel, BorderLayout.WEST);
            
            // Info Panel (name, role, join time)
            JPanel infoPanel = createInfoPanel(participant);
            panel.add(infoPanel, BorderLayout.CENTER);
            
            // Status indicators (muted, video, etc.)
            JPanel statusPanel = createStatusPanel(participant);
            panel.add(statusPanel, BorderLayout.EAST);
            
            return panel;
        }
        
        /**
         * Creates the profile image label.
         *
         * @param participant The participant
         * @return The image label
         */
        private JLabel createProfileImageLabel(final Participant participant) {
            JLabel picLabel = new JLabel();
            picLabel.setPreferredSize(new Dimension(50, 50));
            
            try {
                BufferedImage img = ImageIO.read(
                    new URL(participant.getLogoUrl())
                );
                ImageIcon icon = new ImageIcon(
                    img.getScaledInstance(50, 50, 
                        Image.SCALE_SMOOTH)
                );
                picLabel.setIcon(icon);
            } catch (Exception e) {
                // Use default icon on error
                picLabel.setText(participant.getDisplayName()
                    .substring(0, 1).toUpperCase());
                picLabel.setHorizontalAlignment(JLabel.CENTER);
                picLabel.setBackground(ACCENT_COLOR);
                picLabel.setForeground(Color.WHITE);
                picLabel.setOpaque(true);
                picLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
            }
            
            return picLabel;
        }
        
        /**
         * Creates the info panel with name, role, and join time.
         *
         * @param participant The participant
         * @return The info panel
         */
        private JPanel createInfoPanel(final Participant participant) {
            JPanel panel = new JPanel(new GridBagLayout());
            panel.setOpaque(false);
            
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.anchor = GridBagConstraints.WEST;
            gbc.insets = new Insets(0, 0, 2, 0);
            
            // Name
            JLabel nameLabel = new JLabel(participant.getDisplayName());
            nameLabel.setFont(new Font("Segoe UI Semibold", Font.BOLD, 
                    NAME_FONT_SIZE));
            panel.add(nameLabel, gbc);
            
            // Role
            gbc.gridy = 1;
            gbc.insets = new Insets(0, 0, 0, 0);
            JLabel roleLabel = new JLabel(
                participant.getRole().toString().toLowerCase()
            );
            roleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 
                    DETAIL_FONT_SIZE));
            roleLabel.setForeground(new Color(100, 100, 100));
            panel.add(roleLabel, gbc);
            
            // Join time
            gbc.gridy = 2;
            JLabel timeLabel = new JLabel(
                "Joined at " + participant.getFormattedJoinTime()
            );
            timeLabel.setFont(new Font("Segoe UI", Font.PLAIN, 
                    DETAIL_FONT_SIZE));
            timeLabel.setForeground(new Color(120, 120, 120));
            panel.add(timeLabel, gbc);
            
            return panel;
        }
        
        /**
         * Creates the status panel with indicators.
         *
         * @param participant The participant
         * @return The status panel
         */
        private JPanel createStatusPanel(final Participant participant) {
            JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
            panel.setOpaque(false);
            
            if (participant.isMuted()) {
                JLabel mutedLabel = new JLabel("🔇");
                mutedLabel.setToolTipText("Muted");
                panel.add(mutedLabel);
            }
            
            if (!participant.isVideoOn()) {
                JLabel videoOffLabel = new JLabel("📹");
                videoOffLabel.setToolTipText("Video Off");
                panel.add(videoOffLabel);
            }
            
            if (participant.isHandRaised()) {
                JLabel handLabel = new JLabel("✋");
                handLabel.setToolTipText("Hand Raised");
                panel.add(handLabel);
            }
            
            return panel;
        }
    }
    
    /**
     * Demo main method for testing.
     *
     * @param args Command line arguments
     */
    public static void main(final String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Participants List");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            
            ParticipantsListViewModel viewModel = 
                new ParticipantsListViewModel();

            MeetingSession meetingSession = new MeetingSession("hi@gmail.com", SessionMode.TEST);

            viewModel.loadParticipants(meetingSession);
            
            ParticipantsListView view = new ParticipantsListView(viewModel);
            frame.add(view);
            
            frame.setSize(600, 500);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}


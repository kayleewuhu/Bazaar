package Referee;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import javax.swing.*;

import Common.Data.EquationTable;
import Common.View.GameStateRenderer;
import Tests.JsonObjects.GameStateJson;

// This class is a GUI for the game from the referee's view point. It will
// show the game state by state.
public class GameView extends JFrame {
  // all game states in a game
  List<Game_State> gameStates;
  // the index of the game state that is currently being shown
  int currentIndex;
  // the image component
  private ImagePanel imagePanel;
  // the equations in the game
  private EquationTable equations;

  public GameView(List<Game_State> gameStates, EquationTable equations) {
    this.currentIndex = 0;
    this.gameStates = gameStates;
    this.equations = equations;
    this.initializeWindow();
    this.initializeImagePanel();
    this.initializeButtonPanel();
    this.setVisible(true);
  }

  private void initializeWindow() {
    setTitle("Game Observer");
    setSize(600, 500);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setLayout(new BorderLayout());
  }
  private void initializeImagePanel() {
    this.imagePanel = new ImagePanel();
    this.imagePanel.setPreferredSize(new Dimension(600, 500));
    add(this.imagePanel, BorderLayout.CENTER);
  }
  private void initializeButtonPanel() {
    JPanel buttonPanel = new JPanel();
    JButton nextButton = new JButton("Next");
    JButton prevButton = new JButton("Previous");
    JButton saveButton = new JButton("Save");

    nextButton.addActionListener(e -> showNextState());
    prevButton.addActionListener(e -> showPreviousState());
    saveButton.addActionListener(e -> saveCurrentState());

    buttonPanel.add(prevButton);
    buttonPanel.add(nextButton);
    buttonPanel.add(saveButton);
    add(buttonPanel, BorderLayout.SOUTH);
  }

  // shows the next state of the game when you press the next button
  private void showNextState() {
    if (this.currentIndex < this.gameStates.size() - 1) {
      this.currentIndex++;
      displayGameState(this.gameStates.get(this.currentIndex));
    } else {
      JOptionPane.showMessageDialog(this, "No more states available.", "End of States", JOptionPane.WARNING_MESSAGE);
    }
  }

  // shows the previous state of the game when you press the previous button
  private void showPreviousState() {
    if (this.currentIndex > 0) {
      this.currentIndex--;
      displayGameState(gameStates.get(this.currentIndex));
    } else {
      JOptionPane.showMessageDialog(this, "This is the first state.", "Start of States", JOptionPane.WARNING_MESSAGE);
    }
  }

  // displays the current game state on the gui window
  private void displayGameState(Game_State gameState) {
    BufferedImage image = GameStateRenderer.renderImage(gameState, this.equations);
    imagePanel.setImage(image);
    repaint();
  }

  // saves the current state as a json into a file type in which the user chooses
  private void saveCurrentState() {
    if (gameStates.isEmpty()) return;

    JFileChooser fileChooser = new JFileChooser();
    int choice = fileChooser.showSaveDialog(this);
    if (choice == JFileChooser.APPROVE_OPTION) {
      File file = fileChooser.getSelectedFile();
      Game_State currentGameState = gameStates.get(this.currentIndex);
      GameStateJson gameStateJson = new GameStateJson(currentGameState);
      Gson gson = new GsonBuilder().setPrettyPrinting().create();
      try (FileWriter writer = new FileWriter(file);) {
        gson.toJson(gameStateJson, writer);
        JOptionPane.showMessageDialog(this, "Game state saved successfully.", "Save Successful", JOptionPane.INFORMATION_MESSAGE);
      } catch (IOException ex) {
        JOptionPane.showMessageDialog(this, "Failed to save the game state.", "Save Error", JOptionPane.ERROR_MESSAGE);
      }
    }
  }
  // custom jpanel class for displaying the image
  private static class ImagePanel extends JPanel {
    private BufferedImage image;

    public void setImage(BufferedImage image) {
      this.image = image;
      repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
      super.paintComponent(g);
      if (image != null) {
        // calculate the new dimensions to fit the image within the panel
        int panelWidth = getWidth();
        int panelHeight = getHeight();
        int imgWidth = image.getWidth();
        int imgHeight = image.getHeight();

        double scaleFactor = Math.min((double) panelWidth / imgWidth, (double) panelHeight / imgHeight);

        int newWidth = (int) (imgWidth * scaleFactor);
        int newHeight = (int) (imgHeight * scaleFactor);

        int x = (panelWidth - newWidth) / 2;
        int y = (panelHeight - newHeight) / 2;

        Graphics2D g2d = (Graphics2D) g;
        g2d.drawImage(image, x, y, newWidth, newHeight, this);

      }
    }
  }
}

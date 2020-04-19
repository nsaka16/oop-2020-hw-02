import javax.swing.*;
import java.awt.*;
import java.util.Random;

//Needed help for this class.
public class JBrainTetris extends  JTetris {
    /**
     * Creates a new JTetris where each tetris square
     * is drawn with the given number of pixels.
     *
     * @param pixels
     */
    DefaultBrain brain;
    JCheckBox brainActive;
    Brain.Move goal;
    int brainCount = 0;
    JSlider slider;
    JLabel adversary;

    JBrainTetris(int pixels) {
        super(pixels);
        brain = new DefaultBrain();
        goal = new Brain.Move();
    }

    @Override
    public void tick(int verb)
    {   boolean tck = true;
        if(verb == DOWN)
        {
            //If verb is Down first brain should process activity.
            if(brainActive.isSelected()) tck = brainActivity(verb);
        }
        if(tck)super.tick(verb);
    }

    private boolean brainActivity(int verb)
    {
        if(brainCount != super.count)
        {
            board.undo();
            brainCount = super.count;
            goal = brain.bestMove(board,currentPiece,HEIGHT,goal);
        }
        if(goal==null) {
            super.tick(verb);
            return false;
        }

        //Brain can do only one rotation
        if(!currentPiece.equals(goal.piece))  currentPiece = currentPiece.fastRotation();
        int delta = goal.x - currentX;
        if(delta > 0)
        {
            currentX+=1;
        }else if (delta<0){
            currentX-=1;
        }//If delta is zero do nothing
        return true;
    }

    @Override
    public JComponent createControlPanel()
    {
        JPanel panel = (JPanel) super.createControlPanel();
        panel.add(new JLabel("Brain: "));
        brainActive = new JCheckBox("Brain Active.");
        brainActive.setSelected(false);
        panel.add(brainActive);
        panel.add(addAdversary());
        return panel;
    }

   private JComponent addAdversary()
   {
       JPanel little = new JPanel();
       adversary = new JLabel("Adversary");
       adversary.setPreferredSize(new Dimension(100,15));
       slider = new JSlider(0,100,0);
       little.add(adversary);
       little.add(slider);
       return little;
   }

    @Override
    public Piece pickNextPiece()
    {
        Random rand = new Random();
        String msg = "";
        int randomNumber = rand.nextInt(99);
        int sliderValue = slider.getValue();
        if(randomNumber>sliderValue)
        {
            adversary.setText("*OK*");
            return super.pickNextPiece();
        }
        else{
            return processPieces();
        }
    }

    private Piece processPieces()
    {
        Piece worstPiece = null;
        double worstScore = Integer.MIN_VALUE;
        adversary.setText("OK");
        Brain.Move bestMove = null;
        for(Piece newPiece : pieces)
        {
            bestMove = brain.bestMove(board,newPiece,HEIGHT,bestMove);
            if(bestMove==null) return super.pickNextPiece();
            if(bestMove.score > worstScore)
            {
                worstPiece = newPiece;
                worstScore = bestMove.score;
            }
        }
        return worstPiece;
    }

    public static void main(String[] args) {
        JBrainTetris brainTetris = new JBrainTetris(16);
        JFrame frame = createFrame(brainTetris);
        frame.setVisible(true);
    }
}

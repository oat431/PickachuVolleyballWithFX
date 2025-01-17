import controller.DrawingLoop;
import controller.GameLoop;
import de.saxsys.mvvmfx.testingutils.jfxrunner.JfxRunner;
import javafx.scene.input.KeyCode;
import model.Character;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import view.Platform;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.NoSuchElementException;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;

@RunWith(JfxRunner.class)
public class CharacterTest {
    private Character floatingCharacter;
    private ArrayList<Character> characterListUnderTest;
    private Platform platformUnderTest;
    private GameLoop gameLoopUnderTest;
    private DrawingLoop drawingLoopUnderTest;
    private Method updateMethod;
    private Method redrawMethod;

    @Before
    public void setup(){
        floatingCharacter = new Character(30,30,0,0, KeyCode.A, KeyCode.D,KeyCode.W,KeyCode.S,KeyCode.F);
        characterListUnderTest = new ArrayList<>();
        characterListUnderTest.add(floatingCharacter);
        platformUnderTest = new Platform();
        gameLoopUnderTest = new GameLoop(platformUnderTest);
        drawingLoopUnderTest = new DrawingLoop(platformUnderTest);

        try{
            updateMethod = GameLoop.class.getDeclaredMethod("update" , ArrayList.class);
            redrawMethod = DrawingLoop.class.getDeclaredMethod("paint",ArrayList.class);
            updateMethod.setAccessible(true);
            redrawMethod.setAccessible(true);
        }catch (NoSuchElementException | NoSuchMethodException e){
            e.printStackTrace();
            updateMethod = null;

        }
    }
    @Test
    public void characterInitialValuesShouldMatchConstructorArguments(){
        assertEquals("Initial x", 30, floatingCharacter.getX(), 0);
        assertEquals("Initial y", 30, floatingCharacter.getY(), 0);
        assertEquals("Offset x", 0, floatingCharacter.getOffsetX(), 0.0);
        assertEquals("Offset y", 0, floatingCharacter.getOffsetY(), 0.0);
        assertEquals("Left key", KeyCode.A, floatingCharacter.getLeftKey());
        assertEquals("Right key", KeyCode.D, floatingCharacter.getRightKey());
        assertEquals("Up key", KeyCode.W, floatingCharacter.getUpKey());
    }

    @Test
    public void characterShouldMoveToTheLeftAfterTheLeftKeyIsPressed() throws IllegalAccessException, InvocationTargetException, InvocationTargetException {
            platformUnderTest.getKeys().add(KeyCode.A);
            updateMethod.invoke(gameLoopUnderTest,characterListUnderTest);
            redrawMethod.invoke(drawingLoopUnderTest,characterListUnderTest);
            assertTrue("Controller: Left key pressing is acknowledged",platformUnderTest.getKeys().isPressed(KeyCode.A));
            assertTrue("Model: Character moving left state is set", characterListUnderTest.get(0).isMoveLeft());
            assertTrue("View: Character is moving left", characterListUnderTest.get(0).getX() < characterListUnderTest.get(0).getStartX());
    }

    @Test
    public void characterShouldMoveToTheRightAfterTheRightKeyIsPressed() throws IllegalAccessException, InvocationTargetException, InvocationTargetException {
        platformUnderTest.getKeys().add(KeyCode.D);
        updateMethod.invoke(gameLoopUnderTest,characterListUnderTest);
        redrawMethod.invoke(drawingLoopUnderTest,characterListUnderTest);
        assertTrue("Controller: Left key pressing is acknowledged",platformUnderTest.getKeys().isPressed(KeyCode.D));
        assertTrue("Model: Character moving left state is set", characterListUnderTest.get(0).isMoveRight());
        assertTrue("View: Character is moving left", characterListUnderTest.get(0).getX() > characterListUnderTest.get(0).getStartX());
    }

}

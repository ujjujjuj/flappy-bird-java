public class Main {
    public static void main(String[] args) {
        System.setProperty("sun.java2d.opengl", "true");

        Engine engine = new Engine("Flappy Bird");

        PipeManager pipeManager = new PipeManager();
        Floor floor = new Floor();
        Bird bird = new Bird();
        Score score = new Score();
        StartScreen startScreen = new StartScreen();
        EndScreen endScreen = new EndScreen();
        Flash flash = new Flash();

        engine.add(pipeManager);
        engine.add(bird);
        engine.add(floor);
        engine.add(score);
        engine.add(startScreen);
        engine.add(endScreen);
        engine.add(flash);

        engine.start();
    }
}

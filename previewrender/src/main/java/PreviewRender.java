import se.llbit.chunky.main.Chunky;
import se.llbit.chunky.main.ChunkyOptions;
import se.llbit.chunky.renderer.RenderContext;
import se.llbit.chunky.renderer.RenderManager;
import se.llbit.chunky.renderer.RenderMode;
import se.llbit.chunky.renderer.scene.Scene;
import se.llbit.chunky.renderer.scene.SynchronousSceneManager;
import se.llbit.chunky.resources.TexturePackLoader;
import se.llbit.chunky.world.ChunkPosition;
import se.llbit.chunky.world.World;
import se.llbit.math.QuickMath;
import se.llbit.math.Vector3;
import se.llbit.util.ProgressListener;
import se.llbit.util.TaskTracker;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

/**
 * This demonstrates how to set up a very simple scene and render it
 * in preview rendering mode.
 */
public class PreviewRender {
  public static void main(String[] args) throws InterruptedException, IOException {
    World world = new World(new File("/home/jesper/Downloads/bro3"), false);
    Chunky chunky = new Chunky(ChunkyOptions.getDefaults());
    TexturePackLoader.loadTexturePacks(new String[0], false);
    RenderContext context = new RenderContext(chunky);
    RenderManager renderer = new RenderManager(context, true);

    // The synchronous scene manager is used because we want to wait for
    // all chunks to be loaded before starting the render.
    SynchronousSceneManager sceneManager = new SynchronousSceneManager(context, renderer);
    renderer.setSceneProvider(sceneManager);
    sceneManager.getScene().setName("HP123");

    // Build a list of chunks to load:
    Collection<ChunkPosition> selection = new ArrayList<>();
    for (int x = 0; x < 3; x++){
      for (int z = 0; z < 3; z++){
        selection.add(ChunkPosition.get(x, z));
      }
    }
    sceneManager.loadFreshChunks(world, selection); // Load the chunks.

    Scene scene = sceneManager.getScene();
    scene.setRenderMode(RenderMode.PREVIEW);
    scene.camera().setPosition(new Vector3(24, 76, 24));
    scene.camera().setView(QuickMath.degToRad(244), QuickMath.degToRad(-45), 0);
    scene.setBufferFinalization(true);
    renderer.start();
    renderer.join();
    renderer.getBufferedScene().saveFrame(new File("test.png"), new TaskTracker(ProgressListener.NONE));
  }

}

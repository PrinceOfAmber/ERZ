package teamroots.emberroot.entity.endermini;
import java.util.Random;
import org.lwjgl.opengl.GL11;
import net.minecraft.client.model.ModelEnderman;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import teamroots.emberroot.Const;
import teamroots.emberroot.config.ConfigManager;
import teamroots.emberroot.util.RenderUtil;

public class RenderEnderminy extends RenderLiving<EntityEnderminy> {
  private static final ResourceLocation endermanEyesTexture = new ResourceLocation(Const.MODID, "textures/entity/enderminy_eyes.png");
  private static final ResourceLocation endermanTextures = new ResourceLocation(Const.MODID, "textures/entity/enderminy.png");
  private ModelEnderman endermanModel;
  private Random rnd = new Random();
  public RenderEnderminy(RenderManager rm) {
    super(rm, new ModelEnderman(0), 0.5F);
    endermanModel = (ModelEnderman) super.mainModel;
    addLayer(new EyesLayer());
  }
  public void doRender(EntityEnderminy entity, double x, double y, double z, float entityYaw, float partialTicks) {
    endermanModel.isAttacking = entity.isScreaming();
    if (entity.isScreaming()) {
      double d3 = 0.02D;
      x += rnd.nextGaussian() * d3;
      z += rnd.nextGaussian() * d3;
    }
    super.doRender(entity, x, y, z, entityYaw, partialTicks);
    if (ConfigManager.renderDebugHitboxes)
      RenderUtil.renderEntityBoundingBox(entity, x, y, z);
  }
  protected ResourceLocation getEntityTexture(EntityEnderminy p_110775_1_) {
    return endermanTextures;
  }
  @Override
  protected void preRenderCallback(EntityEnderminy p_77041_1_, float p_77041_2_) {
    GL11.glScalef(0.5F, 0.25F, 0.5F);
  }
  private class EyesLayer implements LayerRenderer<EntityEnderminy> {
    @Override
    public void doRenderLayer(EntityEnderminy em, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw,
        float headPitch, float scale) {
      bindTexture(endermanEyesTexture);
      GlStateManager.enableBlend();
      GlStateManager.disableAlpha();
      GlStateManager.blendFunc(GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE);
      GlStateManager.disableLighting();
      GlStateManager.depthMask(!em.isInvisible());
      int i = 61680;
      int j = i % 65536;
      int k = i / 65536;
      OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float) j, (float) k);
      GlStateManager.enableLighting();
      GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
      getMainModel().render(em, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
      setLightmap(em);
      GlStateManager.depthMask(true);
      GlStateManager.disableBlend();
      GlStateManager.enableAlpha();
    }
    @Override
    public boolean shouldCombineTextures() {
      return false;
    }
  }
  public static class Factory implements IRenderFactory<EntityEnderminy> {
    @Override
    public Render<? super EntityEnderminy> createRenderFor(RenderManager manager) {
      return new RenderEnderminy(manager);
    }
  }
}
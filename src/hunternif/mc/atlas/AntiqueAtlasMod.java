package hunternif.mc.atlas;

import hunternif.mc.atlas.core.ChunkBiomeAnalyzer;
import hunternif.mc.atlas.ext.ExtBiomeDataHandler;
import hunternif.mc.atlas.ext.VillageWatcher;
import hunternif.mc.atlas.item.ItemAtlas;
import hunternif.mc.atlas.item.ItemEmptyAtlas;
import hunternif.mc.atlas.network.CustomPacketHandler;

import java.util.logging.Logger;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;

@Mod(modid=AntiqueAtlasMod.ID, name=AntiqueAtlasMod.NAME, version=AntiqueAtlasMod.VERSION)
@NetworkMod(clientSideRequired=true, serverSideRequired=false, packetHandler=CustomPacketHandler.class, channels={AntiqueAtlasMod.CHANNEL})
public class AntiqueAtlasMod {
	public static final String ID = "antiqueatlas";
	public static final String NAME = "Antique Atlas";
	public static final String VERSION = "@@MOD_VERSION@@";
	public static final String CHANNEL = ID;
	
	@Instance(ID)
	public static AntiqueAtlasMod instance;
	
	public static Logger logger;
	
	@SidedProxy(clientSide="hunternif.mc.atlas.ClientProxy", serverSide="hunternif.mc.atlas.CommonProxy")
	public static CommonProxy proxy;
	
	public static final ExtBiomeDataHandler extBiomeData = new ExtBiomeDataHandler();
	
	public static ItemAtlas itemAtlas;
	public static ItemEmptyAtlas itemEmptyAtlas;

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		logger = event.getModLog();
		proxy.preInit(event);
		
		Configuration config = new Configuration(proxy.getItemConfigFile());
		int atlasItemID = config.getItem("antiqueAtlas", 26949).getInt();
		int emptyAtlasItemID = config.getItem("emptyAntiqueAtlas", 26948).getInt();
		config.save();
		
		itemAtlas = (ItemAtlas) new ItemAtlas(atlasItemID).setUnlocalizedName("antiqueAtlas");
		LanguageRegistry.addName(itemAtlas, "Antique Atlas");
		itemAtlas.setBiomeAnalyzer(ChunkBiomeAnalyzer.instance);
		
		itemEmptyAtlas = (ItemEmptyAtlas) new ItemEmptyAtlas(emptyAtlasItemID)
			.setUnlocalizedName("emptyAntiqueAtlas").setCreativeTab(CreativeTabs.tabTools);
		LanguageRegistry.addName(itemEmptyAtlas, "Empty Antique Atlas");
		
		GameRegistry.addShapelessRecipe(new ItemStack(itemEmptyAtlas), Item.book, Item.compass);
		
	}
	
	@EventHandler
	public void init(FMLInitializationEvent event){
		proxy.init(event);
		
		MinecraftForge.EVENT_BUS.register(extBiomeData);
		GameRegistry.registerPlayerTracker(AntiqueAtlasMod.extBiomeData);
		
		MinecraftForge.EVENT_BUS.register(new VillageWatcher());
	}
	
	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		proxy.postInit(event);
	}
}

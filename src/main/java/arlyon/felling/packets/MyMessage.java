package arlyon.felling.packets;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MyMessage implements IMessage {

    public boolean disableWhenCrouched;
    public boolean disableWhenStanding;

    // A default constructor is always required
    public MyMessage() {
    }

    public MyMessage(boolean disableWhenCrouched, boolean disableWhenStanding) {
        this.disableWhenCrouched = disableWhenCrouched;
        this.disableWhenStanding = disableWhenStanding;
    }

    @Override
    public void toBytes(ByteBuf byteBuf) {
        // Writes the booleans to the buffer
        byteBuf.writeBoolean(disableWhenCrouched);
        byteBuf.writeBoolean(disableWhenStanding);
    }

    @Override
    public void fromBytes(ByteBuf byteBuf) {
        // Reads the booleans back from the buffer (in the same order they were written to it)
        disableWhenCrouched = byteBuf.readBoolean();
        disableWhenStanding = byteBuf.readBoolean();
    }

    public static class Handler implements IMessageHandler<MyMessage, IMessage> {
        @Override
        public IMessage onMessage(MyMessage message, MessageContext ctx) {
            // Always use a construct like this to actually handle your message. This ensures that
            // youre 'handle' code is run on the main Minecraft thread. 'onMessage' itself
            // is called on the networking thread so it is not safe to do a lot of things here.
            System.out.println("HOLY SHIT");
            FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> handle(message, ctx));
            return null;
        }

        private void handle(MyMessage message, MessageContext ctx) {
            // This code is run on the server side. So you can do server-side calculations here
            EntityPlayerMP playerEntity = ctx.getServerHandler().player;
            playerEntity.mcServer.sendMessage(new TextComponentString("Standing: " + message.disableWhenCrouched+ ", Crouched: " + message.disableWhenStanding));
        }
    }

}
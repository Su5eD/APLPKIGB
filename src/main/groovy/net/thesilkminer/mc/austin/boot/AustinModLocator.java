/*
 * This file is part of APLP: KIGB, licensed under the MIT License
 *
 * Copyright (c) 2022 TheSilkMiner
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package net.thesilkminer.mc.austin.boot;

import net.minecraftforge.fml.loading.moddiscovery.AbstractJarFileLocator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.file.Path;
import java.util.Map;
import java.util.stream.Stream;

public final class AustinModLocator extends AbstractJarFileLocator {
    private static final Logger LOGGER = LogManager.getLogger(AustinModLocator.class);
    private static final String NAME = "aplp:mod_locator";

    public AustinModLocator() {
        LOGGER.info("Successfully initialized mod locator {}, ready to inject fake mods", NAME);
    }

    @Override
    public String name() {
        return NAME;
    }
    
    @Override
    public Stream<Path> scanCandidates() {
        return Stream.of(
                BootDataManager.INSTANCE.getOwn().resolve("aplpkigb-1.0.1-plugin.jar"),
                BootDataManager.INSTANCE.getOwn().resolve("aplpkigb-1.0.1-game.jar")
        );
    }

    @Override
    public void initArguments(Map<String, ?> arguments) {

    }
}

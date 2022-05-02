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

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;
import java.util.jar.Manifest;
import java.util.stream.Collectors;

public class BootDataManager {
    static final BootDataManager INSTANCE = new BootDataManager();

    private Path own;
    private String version;
    private Set<String> packages;

    public final String license = "MIT";

    public Path getOwn() {
        if (own == null) {
            try {
                own = Paths.get(getClass().getProtectionDomain().getCodeSource().getLocation().toURI());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return own;
    }

    public String getVersion() {
        if (version == null) {
            try {
                final Path manifestPath = getOwn().resolve("META-INF/MANIFEST.MF");
                final Manifest manifest = new Manifest(Files.newInputStream(manifestPath));

                String implVersion = manifest.getMainAttributes().getValue("Implementation-Version");
                version = implVersion != null ? implVersion : "1.0.0";
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return version;
    }

    public Set<String> getPackages() {
        if (packages == null) {
            try (final var walker = Files.walk(getOwn())) {
                packages = walker
                        .filter(p -> p.getNameCount() > 0)
                        .filter(p -> !p.getName(0).toString().equals("META-INF"))
                        .filter(p -> p.getFileName().toString().endsWith(".class"))
                        .filter(Files::isRegularFile)
                        .map(p -> p.getParent().toString().replace('/', '.'))
                        .filter(s -> !s.isEmpty())
                        .collect(Collectors.toSet());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return packages;
    }

    private BootDataManager() {
    }
}

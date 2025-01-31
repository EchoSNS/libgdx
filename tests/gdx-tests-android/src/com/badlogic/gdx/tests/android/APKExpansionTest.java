/*******************************************************************************
 * Copyright 2011 See AUTHORS file.
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

package com.badlogic.gdx.tests.android;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.backends.android.AndroidFiles;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.tests.utils.GdxTest;

/* 
 * To use this test, an APK expansion file must be present on the device
 * run: gradlew tests:gdx-tests-android:uploadOBB
 * to generate and upload the aforementioned file
 */
public class APKExpansionTest extends GdxTest {
    FileHandleResolver resolver;
    Sound sound;
    SpriteBatch batch;
    Texture texture;
    TextureRegion atlasTextureRegion;
    AssetManager assetManager;

    /** The OBB zip assets are taken from the android tests assets directory
     Extension prefix is prepended to each asset and zipped up into a obb.
     Extension prefix is required so we can be certain the assets are coming
     from the obb, while keeping the structure of the assets file tree for easier testing
     **/
    String extensionPrefix = "obbasset-";

    @Override
    public void create () {
        if ((((AndroidFiles)Gdx.files)).setAPKExpansion(1, 0)) {
            resolver = new ZipFileHandleResolver();
        } else {
            Gdx.app.error("libgdx", "No Expansion can be opened");
        }

        assetManager = new AssetManager();
        assetManager.load("data/" + extensionPrefix + "testpackobb", TextureAtlas.class);
        assetManager.finishLoading();

        sound = Gdx.audio.newSound(Gdx.files.internal("data/" + extensionPrefix + "chirp.ogg"));
        sound.play();
        texture = new Texture(resolver.resolve("data/" + extensionPrefix + "badlogic.jpg"));
        batch = new SpriteBatch();
        TextureAtlas atlas = assetManager.get("data/" + extensionPrefix + "testpackobb");
        atlasTextureRegion = new TextureRegion(atlas.findRegion("water"));
    }

    @Override
    public void render () {
        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        batch.draw(texture, 0, 0);
        batch.draw(atlasTextureRegion, 0, 0);
        batch.end();
    }
}
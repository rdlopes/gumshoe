package org.rdlopes.gumshoe.platform.impl.ext;

import com.sun.jna.Library;

import static com.sun.jna.Native.loadLibrary;

interface CLibrary extends Library {

    CLibrary INSTANCE = (CLibrary) loadLibrary("c", CLibrary.class);

    int getpid();
}
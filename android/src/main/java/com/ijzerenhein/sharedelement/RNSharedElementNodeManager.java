package com.ijzerenhein.sharedelement;

import java.util.Map;
import java.util.HashMap;

import android.view.View;

import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.uimanager.NativeViewHierarchyManager;

public class RNSharedElementNodeManager extends Object {
    private Map<Integer, RNSharedElementNode> mNodes = new HashMap<Integer, RNSharedElementNode>();
    private NativeViewHierarchyManager mNativeViewHierarchyManager;

    public void setNativeViewHierarchyManager(NativeViewHierarchyManager nativeViewHierarchyManager) {
        mNativeViewHierarchyManager = nativeViewHierarchyManager;
    }

    public NativeViewHierarchyManager getNativeViewHierarchyManager() {
        return mNativeViewHierarchyManager;
    }

    public RNSharedElementNode acquire(int reactTag, View view, boolean isParent, ReadableMap styleConfig) {
        synchronized (mNodes) {
            RNSharedElementNode node = mNodes.get(reactTag);
            if (node != null) {
                node.setRefCount(node.getRefCount() + 1);
                return node;
            }
            node = new RNSharedElementNode(reactTag, view, isParent, styleConfig);
            mNodes.put(reactTag, node);
            return node;
        }
    }

    public int release(RNSharedElementNode node) {
        synchronized (mNodes) {
            node.setRefCount(node.getRefCount() - 1);
            if (node.getRefCount() == 0) {
                mNodes.remove(node.getReactTag());
            }
            return node.getRefCount();
        }
    }
}
/*
 * Copyright 2017 Xuqiang ZHENG
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package in.nerd_is.recycler_simplification;

import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Xuqiang ZHENG on 2017/2/25.
 */
public abstract class TypeFactory {

    private final List<Class<?>> classes = new ArrayList<>();
    private final SparseArray<ViewHolderCreator> creators = new SparseArray<>();

    protected TypeFactory() {
        addTypeRules();
    }

    protected void add(Class<?> clazz, ViewHolderCreator creator) {
        boolean added = classes.add(clazz);
        if (added) {
            creators.put(classes.size() - 1, creator);
        } else {
            throw new IllegalStateException("Something strange happens while adding class to list");
        }
    }

    public final int getType(Class<?> clazz) {
        int idx = classes.indexOf(clazz);
        if (idx >= 0) {
            return idx;
        }

        for (int i = 0, classesSize = classes.size(); i < classesSize; i++) {
            if (classes.get(i).isAssignableFrom(clazz)) {
                return i;
            }
        }
        throw new IllegalArgumentException("unknown class: " + clazz.getCanonicalName());
    }

    public final ViewHolder createViewHolder(ViewGroup parent, int type) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return creators.get(type).create(inflater, parent);
    }

    @SuppressWarnings("unchecked")
    public final void bindViewHolder(ViewHolder viewHolder, Object item) {
        viewHolder.render(item);
    }

    /**
     * You should call {@link #add(Class, ViewHolderCreator)} to add maps between
     * class types and view holder types.
     */
    @SuppressWarnings("WeakerAccess")
    protected abstract void addTypeRules();
}
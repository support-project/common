package org.support.project.common.classanalysis;


/**
 * ジェネリクス(型総称)から、そのクラスを無理やり取得する
 * 
 * @author Koda
 *
 * @param <E>
 */
public class GenericAnalysis<E> {

	private Class<E> type;

    public GenericAnalysis(E... e) {
        @SuppressWarnings("unchecked")
        Class<E> type = (Class<E>) e.getClass().getComponentType();
        this.type = type;
    }

    public Class<E> getType() {
        return type;
    }
}

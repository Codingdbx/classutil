package com.github.codingdong.reflection.dialect;

import com.github.codingdong.reflection.AbstractDialect;

/**
 * <p>Description: </p>
 *
 * @author dbx
 * @date 2020/6/17 16:33
 * @since JDK1.8
 */
public class OracleDialect extends AbstractDialect {
    @Override
    public void deal() {
        System.out.println("i am oracle dialect");
    }

    @Override
    public String getAliasName() {
        return "oracle";
    }
}

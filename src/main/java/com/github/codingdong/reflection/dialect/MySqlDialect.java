package com.github.codingdong.reflection.dialect;

/**
 * <p>Description: </p>
 *
 * @author dbx
 * @date 2020/6/17 16:32
 * @since JDK1.8
 */
public class MySqlDialect extends AbstractDialect {
    @Override
    public void deal() {

        System.out.println("i am mysql dialect");
    }

    @Override
    public String getAliasName() {
        return "mysql";
    }
}

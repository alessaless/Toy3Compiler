package Visitors;

import Nodes.BodyOp;
import Nodes.Decl.DefDeclOp;
import Nodes.Decl.ParDeclOp;
import Nodes.Decl.VarDeclOp;
import Nodes.Expr.*;
import Nodes.ProgramOp;
import Nodes.Stat.*;
import SymbolTable.SymbolTable;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class CodeVisitor implements Visitor {
    private static SymbolTable symbolTable;
    private final String fileName;

    private static FileWriter fileWriter;

    public CodeVisitor(String fileName) {
        this.fileName = fileName;
    }
    @Override
    public Object visit(ProgramOp programOp) {
        try {

            String path  = "test_files" + File.separator + "c_out" + File.separator;

            if(!Files.exists(Path.of(path)))
                new File(path).mkdirs();

            File file = new File(path + File.separator + this.fileName + ".c");

            file.createNewFile();
            fileWriter = new FileWriter(file);


            addUtils();


            fileWriter.close();



        } catch (IOException e) {
            throw new RuntimeException(e);
        }




        return null;
    }

    @Override
    public Object visit(BodyOp bodyOp) {
        return null;
    }

    @Override
    public Object visit(DefDeclOp defDeclOp) {
        return null;
    }

    @Override
    public Object visit(VarDeclOp varDeclOp) {
        return null;
    }

    @Override
    public Object visit(Stat stat) {
        return null;
    }

    @Override
    public Object visit(AssignOp assignOp) {
        return null;
    }

    @Override
    public Object visit(ID id) {
        return null;
    }

    @Override
    public Object visit(Expr expr) {
        return null;
    }

    @Override
    public Object visit(WhileOp whileOp) {
        return null;
    }

    @Override
    public Object visit(IfThenOp ifThenOp) {
        return null;
    }

    @Override
    public Object visit(IfThenElseOp ifThenElseOp) {
        return null;
    }

    @Override
    public Object visit(ParDeclOp parDeclOp) {
        return null;
    }

    @Override
    public Object visit(Op op) {
        return null;
    }

    @Override
    public Object visit(ArithOp arithOp) {
        return null;
    }

    @Override
    public Object visit(BoolOp boolOp) {
        return null;
    }

    @Override
    public Object visit(RelOp relOp) {
        return null;
    }

    @Override
    public Object visit(UnaryOp unaryOp) {
        return null;
    }

    @Override
    public Object visit(ReturnOp returnOp) {
        return null;
    }

    @Override
    public Object visit(ReadOp readOp) {
        return null;
    }

    @Override
    public Object visit(WriteOp writeOp) {
        return null;
    }

    @Override
    public Object visit(Const constOp) {
        return null;
    }

    @Override
    public Object visit(FunCallOp funCallOp) {
        return null;
    }

    @Override
    public Object visit(FunCallOpExpr funCallOpExpr) {
        return null;
    }

    @Override
    public Object visit(FunCallOpStat funCallOpStat) {
        return null;
    }


    public void addUtils(){
        try {
            String path = "src" + File.separator +"Visitors"+ File.separator +"UtilsCFunc.c";
            String content = Files.readString(Path.of(path));
            fileWriter.write(content);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

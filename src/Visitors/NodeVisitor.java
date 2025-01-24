package Visitors;

public interface NodeVisitor {
    Object accept(Visitor visitor);
}

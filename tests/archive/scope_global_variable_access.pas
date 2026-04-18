var
  x: integer;

procedure showGlobal();
begin
  writeln(x);
end;

begin
  x := 42;
  showGlobal();
end.

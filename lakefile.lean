import Lake
open Lake DSL

package «soda» where
  -- add package configuration options here

lean_lib «Soda» where
  -- add library configuration options here

@[default_target]
lean_exe «tiles» where
  root := `Soda.tiles.fairness.main.Main


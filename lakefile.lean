import Lake
open Lake DSL

package «tiles» where
  -- add package configuration options here

require mathlib from git
  "https://github.com/leanprover-community/mathlib4.git" @ "v4.29.0"

@[default_target]
lean_lib «Soda» where
  -- add library configuration options here

@[default_target]
lean_exe «tiles» where
  root := `Soda.tiles.fairness.main.Main


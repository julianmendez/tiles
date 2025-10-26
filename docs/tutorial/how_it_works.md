## How it works

**Tiles** is a flexible framework of rigorous representation of functions aimed at clarity and scalability.
The framework is based on concepts of modular design, especially
*encapsulation*, *reusability*, *interchangeability*, *cohesion*, *low coupling*, *scalability*, and *testability*.

With **Tiles**, we refer to:

- a framework where functions can be combined to create descriptions,
- a graphical representation of the framework, and
- an implementation of the framework.


### Tiles

Each block, called *tile*, has a *name*, a *function*, an *input type*, an *output type*, and
*contextual information* (e.g. constants and auxiliary functions).
This is denoted as follows, where α is the input type, β is the output type, and 'name'
is the function name:

|                                    |
|------------------------------------|
| <sub>*α*</sub> name <sub>*β*</sub> |

Graphically, this is represented as:

![name_alpha_beta](name_alpha_beta.png)

If the tile is a constant, the input type is omitted.


### Types

Types in **Tiles** are:

- *Atomic*: `a` (agent), `r` (resource), `m` (measure), `b` (Boolean).
- *Tuple*: ⟨α_1, …, α_n⟩
- *Sequence*: (α)

The type *measure* is a type that represents quantities, like integer numbers and floating point numbers, and may
include an undefined element (⊥).

The type annotation follows [this grammar][type-annotation-grammar].


### Primitive tiles

The primitive tiles are basic blocks, such that their definitions do not depend on other tiles.

Some tiles may be *polymorphic* or *parametric*.
*Polymorphic tiles* are tiles such that the input or output types are generic, such as α, β, and they are instantiated
when they connected to other tiles.
*Parametric tiles* are tiles such that the main function depends on another function φ provided as a parameter.

These are the primitive tiles.

| Function                                         | Implementation               |
|--------------------------------------------------|------------------------------|
| <sub>*(α),(β)*</sub> cross <sub>*(⟨α, β⟩)*</sub> | [CrossTile][CrossTile]       |
| <sub>*(α),(β)*</sub> zip <sub>*(⟨α, β⟩)*</sub>   | [ZipTile][ZipTile]           |
| <sub>*(α)*</sub> distinct <sub>*(α)*</sub>       | [DistinctTile][DistinctTile] |
| <sub>*(α)*</sub> filter φ <sub>*(α)*</sub>       | [FilterTile][FilterTile]     |
| <sub>*(α)*</sub> map φ <sub>*(β)*</sub>          | [MapTile][MapTile]           |
| <sub>*α*</sub> apply φ <sub>*β*</sub>            | [ApplyTile][ApplyTile]       |
| <sub>*(α)*</sub> fold *z* using φ <sub>*β*</sub> | [FoldTile][FoldTile]         |

Some tiles are directly derived from primitive tiles by instantiating some parameter.
For example:

| Function                               | Implementation           |
|----------------------------------------|--------------------------|
| <sub>*(α)*</sub> sum φ <sub>*m*</sub>  | [SumPhiTile][SumPhiTile] |
| <sub>*(m)*</sub> sum <sub>*m*</sub>    | [SumTile][SumTile]       |
| <sub>*(α)*</sub> length <sub>*m*</sub> | [LengthTile][LengthTile] |

The tile

|                                       |
|---------------------------------------|
| <sub>*(α)*</sub> sum φ <sub>*m*</sub> |

is further instantiated to create

|                                     |
|-------------------------------------|
| <sub>*(m)*</sub> sum <sub>*m*</sub> |

and

|                                        |
|----------------------------------------|
| <sub>*(α)*</sub> length <sub>*m*</sub> |


### Constants

The *constants* are special type of tiles that provide the constants in a deterministic way.
For example, the order of elements for all-agent and for all-resources is always known and always the same.

| Function                      | Implementation                     |
|-------------------------------|------------------------------------|
| all-agent <sub>*(a)*</sub>    | [AllAgentTile][AllAgentTile]       |
| all-resource <sub>*(r)*</sub> | [AllResourceTile][AllResourceTile] |


### Composite tiles

A *composite tile* is created by connecting two or more tiles.
Assume that

|                                      |
|--------------------------------------|
| <sub>*α*</sub> tile 1 <sub>*β*</sub> |

is connected to

|                                      |
|--------------------------------------|
| <sub>*β*</sub> tile 2 <sub>*γ*</sub> |

They can be connected together to form the composite tile

|                                                              |
|--------------------------------------------------------------|
| <sub>*α*</sub> tile 1 ⏐ <sub>*β*</sub> tile 2 <sub>*γ*</sub> |

The composite tile of the example can also be denoted as

|                                               |
|-----------------------------------------------|
| <sub>*α*</sub> tile 2 ◦ tile 1 <sub>*γ*</sub> |

Graphically, this is represented as:

![tile_1_to_tile_2](tile_1_to_tile_2.png)

![tile_1_tile_2_block](tile_1_tile_2_block.png)

Some composite tiles receive special names because they are or can be used in multiple places.
For example:

| Function                                      | Implementation                     |
|-----------------------------------------------|------------------------------------|
| <sub>*(a)*</sub> accumulates <sub>*(m)*</sub> | [AccumulatesTile][AccumulatesTile] |
| <sub>*(α)*</sub> exists φ <sub>*b*</sub>      | [ExistsTile][ExistsTile]           |


### Pipelines

Multiple tiles can be connected to create a *pipeline*.
Each pipeline should have a single *start point* and a single *end point*.
To unify the generation of actors and resources, an empty tile is added at the beginning of the pipeline.

Graphically, we can represent [equity][equity-example] with the following pipeline:

![equity0](equity0.png)

[MapTile]: https://github.com/julianmendez/tiles/blob/master/core/src/main/scala/soda/tiles/fairness/tile/primitive/MapTile.soda

[ApplyTile]: https://github.com/julianmendez/tiles/blob/master/core/src/main/scala/soda/tiles/fairness/tile/primitive/ApplyTile.soda

[CrossTile]: https://github.com/julianmendez/tiles/blob/master/core/src/main/scala/soda/tiles/fairness/tile/primitive/CrossTile.soda

[FilterTile]: https://github.com/julianmendez/tiles/blob/master/core/src/main/scala/soda/tiles/fairness/tile/primitive/FilterTile.soda

[DistinctTile]: https://github.com/julianmendez/tiles/blob/master/core/src/main/scala/soda/tiles/fairness/tile/primitive/DistinctTile.soda

[ZipTile]: https://github.com/julianmendez/tiles/blob/master/core/src/main/scala/soda/tiles/fairness/tile/primitive/ZipTile.soda

[FoldTile]: https://github.com/julianmendez/tiles/blob/master/core/src/main/scala/soda/tiles/fairness/tile/primitive/FoldTile.soda

[AllAgentTile]: https://github.com/julianmendez/tiles/blob/master/core/src/main/scala/soda/tiles/fairness/tile/constant/AllAgentTile.soda

[AllResourceTile]: https://github.com/julianmendez/tiles/blob/master/core/src/main/scala/soda/tiles/fairness/tile/constant/AllResourceTile.soda

[AccumulatesTile]: https://github.com/julianmendez/tiles/blob/master/core/src/main/scala/soda/tiles/fairness/tile/composite/AccumulatesTile.soda

[SumPhiTile]: https://github.com/julianmendez/tiles/blob/master/core/src/main/scala/soda/tiles/fairness/tile/derived/fold/SumPhiTile.soda

[SumTile]: https://github.com/julianmendez/tiles/blob/master/core/src/main/scala/soda/tiles/fairness/tile/derived/fold/SumTile.soda

[LengthTile]: https://github.com/julianmendez/tiles/blob/master/core/src/main/scala/soda/tiles/fairness/tile/derived/fold/LengthTile.soda

[ExistsTile]: https://github.com/julianmendez/tiles/blob/master/core/src/main/scala/soda/tiles/fairness/tile/composite/ExistsTile.soda

[type-annotation-grammar]: https://julianmendez.github.io/tiles/grammar.txt

[equity-example]: https://julianmendez.github.io/tiles/example/equity/equity_example.html



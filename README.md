# Springのスコープの挙動を確かめたかった

## overview

Spring Bootで@Scopeを使って設定したインスタンが、  
広いスコープのインスタンス内に狭いスコープのインスタンスに設定された場合の挙動について確認したかった。

## 結果

ちゃんとそれぞれのスコープを守っていい感じに処理される。

```
Controller: f98afab7-9557-43ec-9755-429734efff1d
PCTX: fce145d7-8ddb-4290-9938-a30f0e7e7a69
RCTX: 7068f4c3-2612-44e6-9fc1-5266c37dbe92
SCTX: 92d5c2f0-df3c-4fbb-95cf-00095f043a13
SCTX.RCTX: 7068f4c3-2612-44e6-9fc1-5266c37dbe92
SCTX.PCTX: 7e1370ca-03f0-4565-99f5-4b022da51449
RCTX.PCTX: 65603789-e110-455e-b142-e30705376481
```

```
Controller: f98afab7-9557-43ec-9755-429734efff1d
PCTX: fce145d7-8ddb-4290-9938-a30f0e7e7a69
RCTX: 25171495-a704-48b4-930d-b199d7d6019c
SCTX: 92d5c2f0-df3c-4fbb-95cf-00095f043a13
SCTX.RCTX: 25171495-a704-48b4-930d-b199d7d6019c
SCTX.PCTX: 7e1370ca-03f0-4565-99f5-4b022da51449
RCTX.PCTX: fd678895-076e-4ad0-81a9-d913c613baa1
```


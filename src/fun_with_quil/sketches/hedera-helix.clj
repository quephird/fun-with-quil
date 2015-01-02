(ns fun-with-quil.sketches.hedera-helix
  (:require [quil.core :as q :include-macros true]))

(defn vein [n x y]
  (q/stroke-weight n)
  (q/stroke 229 255 204)
  (q/line 0 0 x y)
  (if (> n 0)
    (doseq [t [-40 40]]
      (let [new-x (* 0.4 x)
            new-y (* 0.4 y)]
      (q/push-matrix)
      (q/translate new-x new-y)
      (q/rotate (q/radians t))
      (vein (dec n) new-x new-y)
      (q/pop-matrix)))))

(defn leaf [s c x y]
  (let [unscaled-vs [[0 0]
                     [-50 30]
                     [-100 20]
                     [-90 -10]
                     [-125 -105]
                     [-50 -105]
                     [0 -180]
                     [50 -105]
                     [125 -105]
                     [90 -10]
                     [100 20]
                     [50 30]]
        leaf-vs      (map (fn[v] (map #(* s %) v)) unscaled-vs)]
    (q/no-stroke)
    (q/push-matrix)
    (q/translate x y)
    (apply q/fill c)
    (q/begin-shape)
    (doseq [v leaf-vs]
      (apply q/vertex v))
    (q/end-shape :close)
        (doseq [[vein-x vein-y] (drop 1 (take-nth 2 leaf-vs))]
      (vein 2 vein-x vein-y))
    (q/pop-matrix)))

(defn branch [n x y]
  (let [leaf-count n
        branch-θ (q/random -10 10)]
    (q/push-matrix)
    (q/translate x y)
    (q/rotate (q/radians branch-θ))
    (q/stroke-weight 3)
    (q/stroke 139 119 101)
    (q/line 0 0 0 (* n n 6))
    (doseq [i (range n 0 -1)]
      (let [s     (* 0.1 i)
            c     [0 (q/random 50 100) 0]
            θ     (if (even? i) (q/random 100 140) (q/random -100 -140))
            dx     (* (if (even? i) 1 -1) 5 i)
            dy    (* 12 i)]
        (q/push-matrix)
        (q/rotate (q/radians θ))
        (leaf s c 0 0)
        (q/pop-matrix)
        (q/translate 0 dy)))
    (q/pop-matrix)))

(defn setup []
  (q/smooth)
  (q/no-loop))

(defn draw []
  (let [fc (q/frame-count)
        w  (q/width)
        h  (q/height)]
    (q/background 0)
    (branch 10 100 0)
    (branch 8 400 0)
    (branch 7 700 0)
    (branch 11 1000 0)
    (branch 9 1300 0))
    (q/save "hedera-helix.png"))

(q/defsketch hedera-helix
  :title "hedera-helix"
  :setup setup
  :draw draw
  :size [1440 800])

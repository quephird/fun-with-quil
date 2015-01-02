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
  (let [stem-length       (* s 40)
        unscaled-vertices [[0 20 -50 30 -50 30]
                           [-50 30 -100 40 -100 20]
                           [-100 20 -100 0 -90 -10]
                           [-110 -10 -130 -105 -125 -105]
                           [-125 -115 -50 -115 -50 -105]
                           [-55 -110 0 -190 0 -180]
                           [0 -190 55 -110 50 -105]
                           [50 -115 125 -115 125 -105]
                           [130 -105 100 -10 90 -10]
                           [100 0 100 20 100 20]
                           [100 40 50 30 50 30]
                           [50 30 0 20 0 0]]
        scaled-vertices    (map (fn[v] (map #(* s %) v)) unscaled-vertices)
        vein-vertices  (->> scaled-vertices
                         (take (dec (count unscaled-vertices)))
                          (drop 1)
                          (take-nth 2)
                          (map #(drop 4 %)))]
    (q/push-matrix)
    (q/translate x y)
    (q/stroke-weight 3)
    (q/stroke 139 119 101)
    (q/line 0 0 0 (- stem-length))

    (q/no-stroke)
    (apply q/fill c)
    (q/translate 0 (- stem-length))
    (q/begin-shape)
    (q/vertex 0 0)
    (doseq [v scaled-vertices]
      (apply q/bezier-vertex v))
    (q/end-shape :close)

    (doseq [[vein-x vein-y] vein-vertices]
      (vein 2 vein-x vein-y))
    (q/pop-matrix)
    ))

(defn branch [n x y]
  (let [leaf-count n
        branch-θ (q/random -10 10)]
    (q/push-matrix)
    (q/translate x y)
    (q/rotate (q/radians branch-θ))
    (q/stroke-weight 3)
    (q/stroke 139 119 101)
    (q/line 0 0 0 (* n n 7))
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
        h  (q/height)
        n  8]
    (q/background 0)

    (dotimes [i n]
      (branch (int (q/random 8 12)) (+ 50 (* i (/ w n)))  (q/random -100 0)))
    (q/save "hedera-helix.png")))

(q/defsketch hedera-helix
  :title "hedera-helix"
  :setup setup
  :draw draw
  :size [1440 800])

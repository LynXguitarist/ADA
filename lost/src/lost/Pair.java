package lost;

import java.util.Objects;

public class Pair<U, V> {

	public final U x;
	public final V y;

	public Pair(U x, V y) {
		this.x = x;
		this.y = y;
	}
	
	public U getX() {
		return x;
	}
	
	public V getY() {
		return y;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;

		Pair<?, ?> pair = (Pair<?, ?>) o;
		if (!x.equals(pair.x))
			return false;
		return y.equals(pair.y);
	}
	
	// Maybe 9? Was 31
	@Override
	public int hashCode() {
        return 9 * Objects.hashCode(x) + Objects.hashCode(y);
    }

	public static <U, V> Pair<U, V> of(U x, V y) {
		return new Pair<>(x, y);
	}
}

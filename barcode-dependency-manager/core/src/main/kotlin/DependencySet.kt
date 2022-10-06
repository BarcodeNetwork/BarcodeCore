interface DependencySet<T> {
    /**
     * DependencyNotation Set 을 구합니다.
     */
    fun getDependencies(): Collection<T>
}
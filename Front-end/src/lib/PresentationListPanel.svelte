<script>
	import PresentationLabel from './PresentationLabel.svelte';
	export let presentations;
</script>

<div class="container">
	{#each presentations as presentation}
		<PresentationLabel {presentation} />
	{/each}
</div>

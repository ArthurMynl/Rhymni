<script>
	export let presentation;
</script>

<div>
	<p id="title">Équipe {presentation.teams[0].number} - Équipe {presentation.teams[1].number}</p>
	<p>{presentation.type}</p>
</div>

<style>
	#title {
		font-size: 1.3em;
	}
</style>
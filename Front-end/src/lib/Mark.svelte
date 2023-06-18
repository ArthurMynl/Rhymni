<script>
	export let mark;
	export let maxMark;
</script>

<div class="container">
	{#if mark === null || mark === undefined}
		<span class="mark">-</span>
	{:else}
		<span class="mark">{mark}</span>
	{/if}
	<span class="mark-max">/{maxMark}</span>
</div>

<style>
	.mark {
		font-size: 20px;
	}
	.mark-max {
		font-size: 15px;
		color: gray;
	}
</style>

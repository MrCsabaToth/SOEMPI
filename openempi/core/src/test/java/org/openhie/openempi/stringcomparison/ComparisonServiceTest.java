/**
 *
 *  Copyright (C) 2010 SYSNET International, Inc. <support@sysnetint.com>
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 *  implied. See the License for the specific language governing
 *  permissions and limitations under the License.
 *
 */
package org.openhie.openempi.stringcomparison;

import java.util.List;

import org.openhie.openempi.context.Context;
import org.openhie.openempi.model.FieldType;
import org.openhie.openempi.service.BaseServiceTestCase;
import org.openhie.openempi.stringcomparison.metrics.DistanceMetric;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

public class ComparisonServiceTest extends BaseServiceTestCase
{
	private StringPair[] stringPairs = {
		new StringPair("odysseas", "odysseus"),
		new StringPair("odysseas", "odysseas"),
		new StringPair("testing", "hesting"),
		new StringPair("test", "TEST"),
		new StringPair("John", "Jon"),
		new StringPair("John", "Jane"),
		new StringPair(null, null),
		new StringPair(null, "test"),
		new StringPair("test", null),
		new StringPair("Javier", "Havier"),
		new StringPair("Martinez", "Marteenez"),

		new StringPair("0", "0"),
		new StringPair("3", "0"),
		new StringPair("0", "3"),
		new StringPair("0d", "0d"),
		new StringPair("3d", "0d"),
		new StringPair("0d", "3d"),
		new StringPair("0.0f", "0.0f"),
		new StringPair("3.0f", "0.0f"),
		new StringPair("0.0f", "3.0f"),
		new StringPair("0.0d", "0.0d"),
		new StringPair("3.0d", "0.0d"),
		new StringPair("0.0d", "3.0d"),
		new StringPair("0.5e-10", "0.5e10"),
		new StringPair("0.5e10", "0.5e-10")
	};

	private BitStreamPair[] bitStreamPairs = {
		new BitStreamPair(new byte[]{0,1,2,3,4,5,6,7,8,9}, new byte[]{10,1,2,3,4,5,6,7,8,9}),
		new BitStreamPair(new byte[]{0,1,2,3,4,5,6,7,8,9}, new byte[]{0,1,2,3,4,5,6,7,8,9}),
		new BitStreamPair(new byte[]{0,1,2,3,4,5,6,7,8,9}, new byte[]{0,1,2,3,4,9,8,7,6,5}),
		new BitStreamPair(new byte[]{0,1,2,3,4,5,6,7,8,9}, new byte[]{9,8,7,6,5,4,3,2,1,0}),
		new BitStreamPair(new byte[]{0,0,0}, new byte[]{-127,-127,-127}),
		new BitStreamPair(null, null),
		new BitStreamPair(null, new byte[]{0,1,2,3,4,5,6,7,8,9}),
		new BitStreamPair(new byte[]{0,1,2,3,4,5,6,7,8,9}, null)
	};
			
	@Override
	protected void onSetUp() throws Exception {
		log.debug("In onSetUp method");
		super.onSetUp();
		setupContext();
	}

	public void testDistanceMetrics() {
		StringComparisonService service = Context.getStringComparisonService();
		DistanceMetricType[] list = service.getDistanceMetricTypes();
		for (DistanceMetricType dmt : list) {
			DistanceMetric metric = dmt.getDistanceMetric();
			System.out.println("=== Testing " + metric.getOutputType().toString() + " Metric " + dmt.getName() + " ===");
			if (metric.getInputType() == FieldType.FieldTypeEnum.String) {
				for (StringPair pair : stringPairs) {
					System.out.println(metric.getOutputType().toString() + " between " + pair.getString1() + " and " + pair.getString2() + " is " +
							metric.score(pair.getString1(), pair.getString2()));
				}
			} else {
				for (BitStreamPair pair : bitStreamPairs) {
					System.out.println(metric.getOutputType().toString() + " between " + pair.getStream1() + " and " + pair.getStream2() + " is " +
							metric.score(pair.getStream1(), pair.getStream2()));
				}
			}
		}
	}
	
	public void testGetComparisonFunctionNames() {
		StringComparisonService service = Context.getStringComparisonService();
		List<String> list = service.getComparisonFunctionNames();
		for (String name : list) {
			DistanceMetricType metricType = service.getDistanceMetricType(name);
			System.out.println("Comparison Function: " + name +
					", InputType: " + metricType.getDistanceMetric().getInputType().toString() +
					", OutputType: " + metricType.getDistanceMetric().getOutputType().toString());
		}
	}

	private class StringPair
	{
		private String string1;
		private String string2;
		
		public String getString1() {
			return string1;
		}

		public void setString1(String string1) {
			this.string1 = string1;
		}

		public String getString2() {
			return string2;
		}

		public void setString2(String string2) {
			this.string2 = string2;
		}

		public StringPair(String string1, String string2) {
			this.string1 = string1;
			this.string2 = string2;
		}

		@Override
		public boolean equals(final Object other) {
			if (!(other instanceof StringPair))
				return false;
			StringPair castOther = (StringPair) other;
			return new EqualsBuilder().append(string1, castOther.string1).append(string2, castOther.string2).isEquals();
		}

		@Override
		public int hashCode() {
			return new HashCodeBuilder().append(string1).append(string2).toHashCode();
		}

		@Override
		public String toString() {
			return new ToStringBuilder(this).append("string1", string1).append("string2", string2).toString();
		}
	}

	private class BitStreamPair
	{
		private byte[] stream1;
		private byte[] stream2;
		
		public byte[] getStream1() {
			return stream1;
		}

		public void setStream1(byte[] stream1) {
			this.stream1 = stream1;
		}

		public byte[] getStream2() {
			return stream2;
		}

		public void setStream2(byte[] stream2) {
			this.stream2 = stream2;
		}

		public BitStreamPair(byte[] stream1, byte[] stream2) {
			this.stream1 = stream1;
			this.stream2 = stream2;
		}

		@Override
		public boolean equals(final Object other) {
			if (!(other instanceof BitStreamPair))
				return false;
			BitStreamPair castOther = (BitStreamPair) other;
			return new EqualsBuilder().append(stream1, castOther.stream1).append(stream2, castOther.stream2).isEquals();
		}

		@Override
		public int hashCode() {
			return new HashCodeBuilder().append(stream1).append(stream2).toHashCode();
		}

		@Override
		public String toString() {
			return new ToStringBuilder(this).append("stream1", stream1).append("stream2", stream2).toString();
		}
		
	}
}
